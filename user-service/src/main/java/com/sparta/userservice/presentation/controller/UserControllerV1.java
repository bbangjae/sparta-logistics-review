package com.sparta.userservice.presentation.controller;

import com.sparta.userservice.application.UserServiceV1;
import com.sparta.userservice.application.dto.request.UserCreateRequest;
import com.sparta.userservice.presentation.dto.response.ApiResponse;
import com.sparta.userservice.presentation.dto.response.UserCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;

    @PostMapping()
    public ResponseEntity<ApiResponse<UserCreateResponse>> create(
            @RequestBody
            @Valid
            UserCreateRequest signupRequest
    ){
        return ApiResponse.created(userServiceV1.create(signupRequest));
    }
}
