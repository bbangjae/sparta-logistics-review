package com.sparta.user_service.presentation.dto.response;

import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.enums.UserRoleEnum;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserRoleChangeResponse {
    private UUID userId;
    private String username;
    private String name;
    private UserRoleEnum role;
    private UserStatusEnum status;
    private String slackId;

    public static UserRoleChangeResponse of(UserEntity user) {
        return new UserRoleChangeResponse(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getRole(),
                user.getStatus(),
                user.getSlackId()
        );
    }
}