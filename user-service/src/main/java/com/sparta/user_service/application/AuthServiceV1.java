package com.sparta.user_service.application;


import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.sparta.user_service.domain.entity.UserEntity;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import com.sparta.user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceV1 {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity login(String username, String rawPassword) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));


        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (user.getStatus() != UserStatusEnum.APPROVED) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        return user;
    }

}
