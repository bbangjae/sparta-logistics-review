package com.sparta.auth_service.infrastructure.client;

import com.example.sparta.common.enums.UserRoleEnum;
import com.sparta.auth_service.infrastructure.config.UserClientErrorDecoder;
import com.sparta.auth_service.presentation.request.SignupRequest;
import com.sparta.auth_service.presentation.response.SignupResponse;
import com.sparta.auth_service.presentation.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "/v1/internal/users", configuration = UserClientErrorDecoder.class)
public interface UserClient {

    @PostMapping() // /v1/internal/users 로 POST
    SignupResponse createUser(@RequestBody SignupRequest request);

    @GetMapping("/by-username")
    UserResponse getUserByUsername(@RequestParam("username") String username);


}