package com.sparta.user_service.application;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.sparta.user_service.presentation.request.UserCreateRequest;
import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.enums.UserRoleEnum;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import com.sparta.user_service.domain.repository.UserRepository;
import com.sparta.user_service.presentation.response.UserCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceV1 {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserCreateResponse create(UserCreateRequest signupRequest){
        String username = signupRequest.getUsername();
        if(userRepository.findByUsername(username).isPresent()){
            throw new BusinessException(ErrorCode.DUPLICATED_USER);
        }

        String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());
        UserEntity user = UserEntity.create(signupRequest, hashedPassword);
        UserEntity savedUser = userRepository.save(user);

        return UserCreateResponse.of(savedUser);
    }

    public UserEntity changeStatus(UUID userId, UserStatusEnum status) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.changeStatus(status);
        return userRepository.save(user);
    }
}
