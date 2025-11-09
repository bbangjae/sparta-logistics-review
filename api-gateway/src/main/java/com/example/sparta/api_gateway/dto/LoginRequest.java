package com.example.sparta.api_gateway.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;
}