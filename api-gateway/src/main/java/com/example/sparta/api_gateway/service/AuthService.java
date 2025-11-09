package com.example.sparta.api_gateway.service;

import com.example.sparta.api_gateway.dto.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;

@Service
public class AuthService {

    private static final String SECRET_KEY_STRING = "my-super-secret-key-my-super-secret-key-1234";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1시간 // 1시간

    private final WebClient webClient = WebClient.create("http://localhost:9006");
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
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String validateTokenAndGetUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}