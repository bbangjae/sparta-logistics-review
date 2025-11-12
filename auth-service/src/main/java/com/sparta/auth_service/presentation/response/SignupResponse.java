package com.sparta.auth_service.presentation.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponse {
    private String username;
    private String name;
    private String slackId;
    private String role;
    private String status;
    private String token;
}
