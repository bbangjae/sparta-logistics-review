package com.example.sparta.common.dto;

import com.example.sparta.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 에러 응답 DTO
 * 
 * 모든 서비스에서 일관된 에러 응답 형식을 제공합니다.
 * 단일 책임 원칙(SRP)에 따라 에러 정보 전달만 담당합니다.
 * 개방-폐쇄 원칙(OCP)에 따라 새로운 에러 정보 추가 시 확장 가능합니다.
 */
@Getter
@Builder
public class ErrorResponse {
    
    /**
     * 에러 발생 시간
     */
    private final LocalDateTime timestamp;
    
    /**
     * HTTP 상태 코드
     */
    private final int status;
    
    /**
     * 에러 코드 (시스템 내부용)
     */
    private final String code;
    
    /**
     * 에러 메시지 (사용자용)
     */
    private final String message;
    
    /**
     * 요청 경로
     */
    private final String path;
    
    /**
     * 필드별 상세 에러 정보 (validation 에러 등)
     */
    private final List<FieldError> fieldErrors;
    
    /**
     * ErrorCode enum으로부터 ErrorResponse 생성
     * 
     * @param errorCode 에러 코드
     * @param path 요청 경로
     * @return ErrorResponse 인스턴스
     */
    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(path)
                .build();
    }
    
    /**
     * ErrorCode와 커스텀 메시지로 ErrorResponse 생성
     * 
     * @param errorCode 에러 코드
     * @param message 커스텀 메시지
     * @param path 요청 경로
     * @return ErrorResponse 인스턴스
     */
    public static ErrorResponse of(ErrorCode errorCode, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .message(message)
                .path(path)
                .build();
    }
    
    /**
     * 필드 에러 정보를 포함한 ErrorResponse 생성
     * 
     * @param errorCode 에러 코드
     * @param path 요청 경로
     * @param fieldErrors 필드 에러 목록
     * @return ErrorResponse 인스턴스
     */
    public static ErrorResponse of(ErrorCode errorCode, String path, List<FieldError> fieldErrors) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(path)
                .fieldErrors(fieldErrors)
                .build();
    }
    
    /**
     * 필드별 에러 정보
     */
    @Getter
    @Builder
    public static class FieldError {
        
        /**
         * 에러가 발생한 필드명
         */
        private final String field;
        
        /**
         * 입력받은 값
         */
        private final Object rejectedValue;
        
        /**
         * 에러 메시지
         */
        private final String message;
        
        /**
         * 필드 에러 생성
         * 
         * @param field 필드명
         * @param rejectedValue 거부된 값
         * @param message 에러 메시지
         * @return FieldError 인스턴스
         */
        public static FieldError of(String field, Object rejectedValue, String message) {
            return FieldError.builder()
                    .field(field)
                    .rejectedValue(rejectedValue)
                    .message(message)
                    .build();
        }
    }
}