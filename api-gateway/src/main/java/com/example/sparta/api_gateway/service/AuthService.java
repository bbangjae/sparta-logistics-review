package com.example.sparta.api_gateway.service;

import com.example.sparta.api_gateway.dto.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private final WebClient webClient = WebClient.create("http://localhost:9006");

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    // user-service에 로그인 요청하고 결과(boolean) 받음
    public Mono<Boolean> validateUser(LoginRequest request) {
        return webClient.post()
                .uri("/v1/auth/login") // user-service login endpoint
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(e -> Mono.just(false));
    }


    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
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