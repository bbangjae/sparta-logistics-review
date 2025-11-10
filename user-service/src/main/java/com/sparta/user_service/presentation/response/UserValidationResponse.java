package com.sparta.user_service.presentation.response;

import com.example.sparta.common.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserValidationResponse {
    private UUID userId;
    private String username;
    private UserRoleEnum role;
    private boolean valid;
}