package com.sparta.userservice.application.exception;

public class DuplicateUserException extends GlobalException {

    public DuplicateUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
