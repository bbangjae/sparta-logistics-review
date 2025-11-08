package com.sparta.user_service.presentation.response;

import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.enums.UserRoleEnum;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserApprovalResponse {
    private UUID userId;
    private String username;
    private String name;
    private UserStatusEnum status;
    private String slackId;
    private UserRoleEnum role;

    public static UserApprovalResponse of(UserEntity user) {
        return new UserApprovalResponse(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getStatus(),
                user.getSlackId(),
                user.getRole() // role이 아직 없으면 null 가능
        );
    }
}
