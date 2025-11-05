package com.sparta.userservice.domain.entity;

import com.sparta.userservice.application.dto.request.UserCreateRequest;
import com.sparta.userservice.domain.enums.UserRoleEnum;
import com.sparta.userservice.domain.enums.UserStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="p_users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column()
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String slackId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private UserStatusEnum status = UserStatusEnum.PENDING;

    public static UserEntity create(UserCreateRequest userCreateRequest, String encodedPassword){
        return UserEntity.builder()
                .role(userCreateRequest.getRole())
                .username(userCreateRequest.getUsername())
                .name(userCreateRequest.getName())
                .password(encodedPassword)
                .slackId(userCreateRequest.getSlackId())
                .build();
    }
}

