package com.sparta.auth_service.presentation.controller;

import com.sparta.auth_service.application.AuthServiceV1;
import com.sparta.auth_service.presentation.request.LoginRequest;
import com.sparta.auth_service.presentation.request.SignupRequest;
import com.sparta.auth_service.presentation.response.LoginResponse;
import com.sparta.auth_service.presentation.response.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthControllerV1 {
    private final AuthServiceV1 authServiceV1;

    @PostMapping("/signup")
    public Mono<ResponseEntity<SignupResponse>> signup(@RequestBody SignupRequest request) {
        return authServiceV1.signup(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request) {
        return authServiceV1.login(request.getUsername(), request.getPassword())
                .map(ResponseEntity::ok);
    }
}
