package com.sparta.user_service.presentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.enums.UserRoleEnum;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserSearchResponse {
    private String username;
    private String name;
    private String slackId;
    private UserRoleEnum role;
    private UserStatusEnum status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static UserSearchResponse of(UserEntity user) {
        return UserSearchResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .slackId(user.getSlackId())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
