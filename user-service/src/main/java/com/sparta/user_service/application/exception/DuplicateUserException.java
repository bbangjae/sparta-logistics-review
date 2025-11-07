package com.sparta.user_service.application.exception;

public class DuplicateUserException extends GlobalException {

    public DuplicateUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
