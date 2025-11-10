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
                .flatMap(response -> {
                    if (!response.isValid()) {
                        return Mono.error(new BusinessException(ErrorCode.INVALID_CREDENTIALS));
                    }

                    String token = authService.generateToken(
                            response.getUserId(),
                            response.getUsername(),
                            response.getRole()
                    );

                    return Mono.just(ResponseEntity.ok(new JwtResponse(
                            token,
                            response.getUserId(),
                            response.getUsername(),
                            response.getRole()
                    )));
                })
                .onErrorResume(e -> {
                    if (e instanceof BusinessException be) {
                        return Mono.error(be);
                    } else if (e instanceof WebClientResponseException.NotFound) {
                        return Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND));
                    }
                    return Mono.error(new BusinessException(ErrorCode.SERVICE_UNAVAILABLE));
                });
    }

}