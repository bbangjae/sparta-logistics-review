package com.sparta.user_service.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    DUPLICATE_USER(HttpStatus.CONFLICT, "중복 된 유저입니다.");

    private final HttpStatus status;
    private final String message;
}
