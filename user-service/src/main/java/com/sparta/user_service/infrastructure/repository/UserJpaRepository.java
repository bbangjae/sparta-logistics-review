package com.sparta.user_service.infrastructure.repository;

import com.sparta.user_service.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository  extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
}
