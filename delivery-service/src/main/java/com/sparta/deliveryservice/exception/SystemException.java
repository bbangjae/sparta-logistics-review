package com.sparta.deliveryservice.exception;

/**
 * 시스템 수준의 예외 (DB 오류, 직렬화 실패 등)
 * 트랜잭션 롤백을 유도해야 하는 경우
 */
public class SystemException extends RuntimeException {

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
