package com.example.sparta.product_service.client;

import com.sparta.user_service.presentation.response.LoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {
    
    @GetMapping("/v1/auth/validate")
    LoginResponse validateToken(@RequestHeader("Authorization") String token);
}