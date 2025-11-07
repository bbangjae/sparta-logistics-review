package com.sparta.user_service.infrastructure.repository;

import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity save(UserEntity user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

}
