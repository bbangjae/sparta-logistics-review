package com.example.sparta.api_gateway.dto;

import com.example.sparta.common.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private UUID userId;
    private String username;
    private UserRoleEnum role;
    private boolean valid;
}