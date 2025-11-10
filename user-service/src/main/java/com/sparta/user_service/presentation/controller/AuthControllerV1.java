package com.sparta.user_service.presentation.controller;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.sparta.user_service.application.AuthServiceV1;
import com.sparta.user_service.domain.repository.UserRepository;
import com.sparta.user_service.presentation.request.LoginRequest;
import com.sparta.user_service.presentation.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthControllerV1 {
    private final AuthServiceV1 authServiceV1;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        var user = authServiceV1.login(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(
                LoginResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .role(user.getRole())
                        .valid(true)
                        .build()
        );
    }
}
