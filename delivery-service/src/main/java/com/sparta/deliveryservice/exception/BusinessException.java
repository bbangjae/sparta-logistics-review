package com.sparta.deliveryservice.exception;

/**
 * 비즈니스 로직 상의 예외 (정상적인 실패 시나리오)
 * 예: 허브 응답 실패, 잘못된 경로 요청 등
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
