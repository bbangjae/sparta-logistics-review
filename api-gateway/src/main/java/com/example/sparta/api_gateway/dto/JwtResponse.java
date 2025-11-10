package com.example.sparta.api_gateway.dto;

import com.example.sparta.common.enums.UserRoleEnum;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private final String token;
    private final UUID userId;
    private final String username;
    private UserRoleEnum role;
    private final String type = "Bearer";
}