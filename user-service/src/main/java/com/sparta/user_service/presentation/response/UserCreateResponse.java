package com.sparta.user_service.presentation.response;

import com.sparta.user_service.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserCreateResponse {
    private String username;
    private String name;
    private String slackId;

    public static UserCreateResponse of(UserEntity userEntity){
        return UserCreateResponse.builder()
                .username(userEntity.getUsername())
                .name(userEntity.getName())
                .slackId(userEntity.getSlackId())
                .build();
    }
}
