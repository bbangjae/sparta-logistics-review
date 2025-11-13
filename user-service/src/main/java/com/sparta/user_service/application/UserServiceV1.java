package com.sparta.user_service.application;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.sparta.user_service.presentation.request.UserCreateRequest;
import com.sparta.user_service.domain.entity.UserEntity;
import com.example.sparta.common.enums.UserRoleEnum;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import com.sparta.user_service.domain.repository.UserRepository;
import com.sparta.user_service.presentation.request.UserUpdateRequest;
import com.sparta.user_service.presentation.response.UserCreateResponse;
import com.sparta.user_service.presentation.response.UserSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceV1 {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserCreateResponse  create(UserCreateRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = UserEntity.create(request, encodedPassword);
        userRepository.save(user);
        return UserCreateResponse.of(user);
    }

    // -------------------------
    // username으로 조회
    // -------------------------
    @Transactional(readOnly = true)
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public UserEntity changeStatus(UUID userId, UserStatusEnum status) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getStatus() == status) {
            throw new BusinessException(ErrorCode.INVALID_STATUS_CHANGE);
        }

        user.changeStatus(status);
        return user;
    }

    @Transactional
    public UserEntity changeRole(UUID userId, UserRoleEnum role) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->  new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getRole() == role) {
            throw new BusinessException(ErrorCode.INVALID_ROLE_CHANGE);
        }

        user.changeRole(role);
        return user;
    }

    @Transactional(readOnly = true)
    public Page<UserSearchResponse> searchUsers(
            String name,
            String slackId,
            UserRoleEnum role,
            UserStatusEnum status,
            Pageable pageable
    ) {
        int pageSize = Math.min(pageable.getPageSize(), 50);
        if (pageSize != 10 && pageSize != 30 && pageSize != 50) {
            pageSize = 10;
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());

        return userRepository.searchUsers(name, slackId, role, status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserSearchResponse> searchUsersByUserId(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return new PageImpl<>(List.of(UserSearchResponse.of(user)));
    }

    @Transactional
    public void deleteUser(UUID userId, Long deletedBy) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            throw new BusinessException(ErrorCode.ALREADY_DELETED_USER);
        }

        user.delete(deletedBy);
    }

    @Transactional
    public UserEntity updateUser(UUID userId, UserUpdateRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.updateUserInfo(
                request.getName(),
                request.getSlackId()
        );

        return user;
    }
}
