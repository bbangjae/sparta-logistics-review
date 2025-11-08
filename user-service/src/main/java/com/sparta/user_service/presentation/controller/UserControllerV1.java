package com.sparta.user_service.presentation.controller;

import com.sparta.user_service.application.UserServiceV1;
import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.enums.UserRoleEnum;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import com.sparta.user_service.presentation.request.UserCreateRequest;
import com.sparta.user_service.presentation.response.UserApprovalResponse;
import com.sparta.user_service.presentation.response.UserCreateResponse;
import com.sparta.user_service.presentation.response.UserRoleChangeResponse;
import com.sparta.user_service.presentation.response.UserSearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;

    private final PagedResourcesAssembler<UserSearchResponse> assembler;

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

    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserRoleChangeResponse> changeRole(
            @PathVariable UUID userId,
            @RequestParam UserRoleEnum role
    ){
        UserEntity updatedUser = userServiceV1.changeRole(userId, role);
        UserRoleChangeResponse response = UserRoleChangeResponse.of(updatedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String slackId,
            @RequestParam(required = false) UserRoleEnum role,
            @RequestParam(required = false) UserStatusEnum status,
            Pageable pageable
    ) {
        Page<UserSearchResponse> page = userServiceV1.searchUsers(name, slackId, role, status, pageable);
        return ResponseEntity.ok(assembler.toModel(page));
    }
}
