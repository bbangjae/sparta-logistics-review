package com.sparta.userservice.domain.repository;

import com.sparta.userservice.domain.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {

    UserEntity save(UserEntity user);

    Optional<UserEntity> findByUsername(String username);
}
