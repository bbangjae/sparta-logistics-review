package com.sparta.user_service.presentation.controller;

import com.example.sparta.common.enums.UserRoleEnum;
import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.sparta.user_service.application.UserServiceV1;
import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.repository.UserRepository;
import com.sparta.user_service.presentation.request.UserCreateRequest;
import com.sparta.user_service.presentation.response.UserCreateResponse;
import com.sparta.user_service.presentation.response.UserInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/internal/users")
public class InternalUserControllerV1 {

    private final UserServiceV1 userServiceV1;

    @GetMapping("/by-username")
    public UserInfoResponse findByUsername(@RequestParam String username) {
        UserEntity user = userServiceV1.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        return UserInfoResponse.of(user);
    }

    @PostMapping
    public ResponseEntity<UserCreateResponse > create(@RequestBody @Valid UserCreateRequest request) {
        UserCreateResponse  response = userServiceV1.create(request);
        return ResponseEntity.ok(response);
    }
}