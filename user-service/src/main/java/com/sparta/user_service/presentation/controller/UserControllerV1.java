package com.sparta.user_service.presentation.controller;

import com.sparta.user_service.application.UserServiceV1;
import com.sparta.user_service.presentation.request.UserCreateRequest;
import com.sparta.user_service.presentation.response.UserCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
