package com.sparta.user_service.application;

import com.sparta.user_service.application.dto.request.UserCreateRequest;
import com.sparta.user_service.application.exception.DuplicateUserException;
import com.sparta.user_service.application.exception.ErrorCode;
import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.repository.UserRepository;
import com.sparta.user_service.presentation.dto.response.UserCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class UserServiceV1 {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserServiceV1.class);

    public UserCreateResponse create(UserCreateRequest signupRequest){
        String username = signupRequest.getUsername();
        if(userRepository.findByUsername(username).isPresent()){
            throw new DuplicateUserException(ErrorCode.DUPLICATE_USER);
        }

        String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());
        UserEntity user = UserEntity.create(signupRequest, hashedPassword);
        UserEntity savedUser = userRepository.save(user);

        return UserCreateResponse.of(savedUser);
    }
}
