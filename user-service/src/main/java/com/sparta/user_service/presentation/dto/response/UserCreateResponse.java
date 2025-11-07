package com.sparta.user_service.presentation.dto.response;

import com.sparta.user_service.domain.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserCreateResponse {

    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String slackId;

    public static UserCreateResponse of(UserEntity userEntity){
        return UserCreateResponse.builder()
                .username(userEntity.getUsername())
                .name(userEntity.getName())
                .slackId(userEntity.getSlackId())
                .build();
    }
}
