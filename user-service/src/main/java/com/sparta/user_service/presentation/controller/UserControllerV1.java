package com.sparta.user_service.presentation.controller;

import com.sparta.user_service.application.UserServiceV1;
import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import com.sparta.user_service.presentation.request.UserCreateRequest;
import com.sparta.user_service.presentation.response.UserApprovalResponse;
import com.sparta.user_service.presentation.response.UserCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;

    @PostMapping()
    public ResponseEntity<UserCreateResponse> create(
            @RequestBody
            @Valid
            UserCreateRequest signupRequest
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(userServiceV1.create(signupRequest));
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserApprovalResponse> changeStatus(
            @PathVariable UUID userId,
            @RequestParam UserStatusEnum status
    ){
        UserEntity updatedUser = userServiceV1.changeStatus(userId, status);
        UserApprovalResponse response = UserApprovalResponse.of(updatedUser);
        return ResponseEntity.ok(response);
    }
}
