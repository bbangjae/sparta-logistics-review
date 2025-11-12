package com.sparta.user_service.application;


import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import com.sparta.user_service.domain.repository.UserRepository;
import com.sparta.user_service.presentation.response.LoginResponse;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    public Mono<LoginResponse> login(String username, String password) {
        return Mono.fromCallable(() ->
                        userRepository.findByUsername(username)
                                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND))
                )
                .subscribeOn(Schedulers.boundedElastic()) // blocking DB 호출을 별도 스레드에서 처리
                .flatMap(user -> {
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new BusinessException(ErrorCode.INVALID_CREDENTIALS));
                    }
                    if (user.getStatus() != UserStatusEnum.APPROVED) {
                        return Mono.error(new BusinessException(ErrorCode.UNAUTHORIZED));
                    }

                    String token = Jwts.builder()
                            .claim("user_id", user.getUserId().toString())
                            .claim("username", user.getUsername())
                            .claim("role", user.getRole().name())
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

                    return Mono.just(response);
                })
                .timeout(Duration.ofSeconds(3))
                .onErrorMap(throwable -> {
                    if (throwable instanceof java.util.concurrent.TimeoutException) {
                        return new BusinessException(ErrorCode.REQUEST_TIMEOUT);
                    }
                    return throwable;
                });

    }
}