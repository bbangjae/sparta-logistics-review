package com.sparta.auth_service.application;


import com.example.sparta.common.enums.UserRoleEnum;
import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.sparta.auth_service.infrastructure.client.UserClient;
import com.sparta.auth_service.presentation.request.SignupRequest;
import com.sparta.auth_service.presentation.response.LoginResponse;
import com.sparta.auth_service.presentation.response.SignupResponse;
import com.sparta.auth_service.presentation.response.UserResponse;
import feign.FeignException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.Key;
import java.time.Duration;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class AuthServiceV1 {

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // -------------------------
    // 회원가입
    // -------------------------
    // 회원가입 + JWT 발급
    public Mono<SignupResponse> signup(SignupRequest request) {
        System.out.println("[signup] >>> 호출됨");
        System.out.println("[signup] 요청 데이터: " + request);

        SignupRequest signupRequest = SignupRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .name(request.getName())
                .slackId(request.getSlackId())
                .hubId(request.getHubId())
                .companyId(request.getCompanyId())
                .deliveryId(request.getDeliveryId())
                .build();

        System.out.println("[signup] 암호화 완료된 SignupRequest 생성됨: " + signupRequest);

        return Mono.fromCallable(() -> {
                    System.out.println("[signup] userClient.createUser() 호출 시작");
                    var response = userClient.createUser(signupRequest);
                    System.out.println("[signup] userClient.createUser() 응답 수신 완료: " + response);
                    return response;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofSeconds(3))
                .map(response -> {
                    System.out.println("[signup] 응답 매핑 시작");

                    // JWT 토큰 발급
                    String token = null;
                    try {
                        token = Jwts.builder()
                                .claim("username", response.getUsername())
                                .claim("role", response.getRole())
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                                .compact();
                        System.out.println("[signup] JWT 토큰 생성 성공: " + token);
                    } catch (Exception e) {
                        System.out.println("[signup] JWT 생성 중 오류 발생: " + e.getMessage());
                        e.printStackTrace();
                    }

                    SignupResponse signupResponse = SignupResponse.builder()
                            .username(response.getUsername())
                            .name(response.getName())
                            .slackId(response.getSlackId())
                            .role(response.getRole())
                            .status(response.getStatus())
                            .token(token)
                            .build();

                    System.out.println("[signup] 최종 SignupResponse 생성됨: " + signupResponse);
                    return signupResponse;
                })
                .doOnSubscribe(sub -> System.out.println("[signup] Mono 구독 시작"))
                .doOnSuccess(resp -> System.out.println("[signup] 성공적으로 완료됨: " + resp))
                .doOnError(err -> {
                    System.out.println("[signup] 에러 발생: " + err.getClass().getSimpleName());
                    System.out.println("[signup] 에러 메시지: " + err.getMessage());
                    err.printStackTrace();
                })
                .onErrorMap(throwable -> {
                    System.out.println("[signup] onErrorMap 진입: " + throwable);
                    if (throwable instanceof java.util.concurrent.TimeoutException) {
                        System.out.println("[signup] TimeoutException 발생");
                        return new BusinessException(ErrorCode.REQUEST_TIMEOUT);
                    } else if (throwable instanceof FeignException) {
                        System.out.println("[signup] FeignException 발생: " + throwable.getMessage());
                        return new BusinessException(ErrorCode.SERVICE_UNAVAILABLE, throwable.getMessage());
                    }
                    return throwable;
                });
    }


    // -------------------------
    // 로그인
    // -------------------------
    public Mono<LoginResponse> login(String username, String password) {
        return Mono.fromCallable(() -> {
                    System.out.println("[1] 로그인 Feign 호출 시작: " + username);
                    UserResponse user = userClient.getUserByUsername(username);
                    System.out.println("[2] 로그인 Feign 호출 완료: " + user);
                    return user;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(user -> {
                    System.out.println("[3] Password 검증 전: " + user.getUsername());
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        System.out.println("[4] Password 불일치");
                        return Mono.error(new BusinessException(ErrorCode.INVALID_CREDENTIALS));
                    }
                    System.out.println("[5] Password 일치");

                    if (!user.isApproved()) {
                        System.out.println("[6] 승인되지 않은 사용자");
                        return Mono.error(new BusinessException(ErrorCode.UNAUTHORIZED));
                    }

                    String token = Jwts.builder()
                            .claim("user_id", user.getUserId().toString())
                            .claim("username", user.getUsername())
                            .claim("role", user.getRole().name())
                            .claim("hub_id", user.getHubId() != null ? user.getHubId().toString() : null)
                            .claim("company_id", user.getCompanyId() != null ? user.getCompanyId().toString() : null)
                            .claim("delivery_id", user.getDeliveryId() != null ? user.getDeliveryId().toString() : null)
                            .setIssuedAt(new Date())
                            .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                            .compact();

                    LoginResponse response = LoginResponse.builder()
                            .userId(user.getUserId())
                            .username(user.getUsername())
                            .role(user.getRole())
                            .token(token)
                            .valid(true)
                            .build();

                    System.out.println("[7] LoginResponse 생성 완료: " + response);
                    return Mono.just(response);
                })
                .timeout(Duration.ofSeconds(3))
                .onErrorMap(throwable -> {
                    System.out.println("[ERROR] 로그인 예외 발생: " + throwable);
                    if (throwable instanceof java.util.concurrent.TimeoutException) {
                        return new BusinessException(ErrorCode.REQUEST_TIMEOUT);
                    }
                    return throwable;
                });
    }
}