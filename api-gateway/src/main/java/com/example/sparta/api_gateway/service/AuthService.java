package com.example.sparta.api_gateway.service;

import com.example.sparta.api_gateway.dto.LoginRequest;
import com.example.sparta.api_gateway.dto.LoginResponse;
import com.example.sparta.common.enums.UserRoleEnum;
import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private final WebClient webClient;

    public AuthService(WebClient.Builder builder,
                       @Value("${jwt.secret}") String secretKeyString,
                       @Value("${jwt.expiration}") long expirationMs) {


        this.secretKeyString = secretKeyString;
        this.expirationMs = expirationMs;

        this.webClient = builder
                .baseUrl("http://user-service")
                .build();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    // user-service 호출
    public Mono<LoginResponse> validateUser(LoginRequest request) {
        return webClient.post()
                .uri("/v1/auth/login")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new BusinessException(ErrorCode.INVALID_CREDENTIALS)))
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new BusinessException(ErrorCode.SERVICE_UNAVAILABLE)))
                )
                .bodyToMono(LoginResponse.class);
    }


    public String generateToken(UUID userId, String username, UserRoleEnum role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("user_id",userId)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateTokenAndGetUsername(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}