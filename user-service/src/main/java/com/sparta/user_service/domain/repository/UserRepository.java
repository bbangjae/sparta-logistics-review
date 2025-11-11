package com.sparta.user_service.domain.repository;

import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.enums.UserRoleEnum;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import com.sparta.user_service.presentation.response.UserSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UserEntity save(UserEntity user);

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findById(UUID userid);

    Page<UserSearchResponse> searchUsers(
            String name,
            String slackId,
            UserRoleEnum role,
            UserStatusEnum status,
            Pageable pageable
    );
}
