package com.sparta.user_service.domain.repository;

import com.sparta.user_service.domain.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UserEntity save(UserEntity user);

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findById(UUID userid);
}
