package com.sparta.deliveryservice.exception;

import java.util.UUID;

// 나중에 common모듈로 뺄 수 있음
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, UUID id) {
        super(String.format("%s을(를) 찾을 수 없습니다. (ID: %s)", entityName, id.toString()));
    }
}
