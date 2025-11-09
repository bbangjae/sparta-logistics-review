package com.example.sparta.api_gateway.controller;

import com.example.sparta.api_gateway.dto.JwtResponse;
import com.example.sparta.api_gateway.dto.LoginRequest;
import com.example.sparta.api_gateway.service.AuthService;
import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<JwtResponse>> login(@RequestBody LoginRequest request) {
        return authService.validateUser(request)
                .flatMap(valid -> {
                    if (valid) {
                        String token = authService.generateToken(request.getUsername());
                        return Mono.just(ResponseEntity.ok(new JwtResponse(token)));
                    } else {
                        return Mono.error(new BusinessException(ErrorCode.INVALID_CREDENTIALS));
                    }
                })
                .onErrorResume(e -> {
                    // WebClient 연결 문제나 404 예외 처리
                    if (e instanceof WebClientResponseException.NotFound) {
                        return Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND));
                    } else if (e instanceof BusinessException) {
                        return Mono.error(e); // 그대로 통과
                    } else {
                        return Mono.error(new BusinessException(ErrorCode.SERVICE_UNAVAILABLE));
                    }
                });
    }

}