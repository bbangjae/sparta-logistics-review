package com.sparta.user_service.presentation.response;

import com.example.sparta.common.enums.UserRoleEnum;
import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserCreateResponse {
    private String username;
    private String name;
    private String slackId;
    private UserRoleEnum role;
    private UserStatusEnum status;

    public static UserCreateResponse of(UserEntity userEntity) {
        return UserCreateResponse.builder()
                .username(userEntity.getUsername())
                .name(userEntity.getName())
                .slackId(userEntity.getSlackId())
                .role(userEntity.getRole())
                .status(userEntity.getStatus())
                .build();
    }
}
