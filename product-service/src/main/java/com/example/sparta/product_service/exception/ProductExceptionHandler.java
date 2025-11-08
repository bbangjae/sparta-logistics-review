package com.example.sparta.product_service.exception;

import com.example.sparta.common.dto.ErrorResponse;
import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Product Service 전역 예외 처리기
 * 
 * product-service에서 발생하는 예외를 일관되게 처리합니다.
 * common 모듈의 ErrorCode, ErrorResponse를 재사용하여 일관성을 유지하면서도
 * 서비스별 커스터마이징이 가능한 구조입니다.
 */
@Slf4j
@RestControllerAdvice
public class ProductExceptionHandler {
    
    /**
     * 비즈니스 로직 예외 처리
     * 
     * BusinessException을 적절한 HTTP 응답으로 변환합니다.
     * ErrorCode enum에 정의된 HTTP 상태 코드와 메시지를 사용합니다.
     * 
     * @param ex 비즈니스 예외
     * @param request HTTP 요청 정보
     * @return 에러 응답
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        ErrorCode errorCode = ex.getErrorCode();
        String requestURI = request.getRequestURI();
        
        // 클라이언트 에러는 WARN, 서버 에러는 ERROR 로그
        if (errorCode.isClientError()) {
            log.warn("[Product Service] 비즈니스 예외 발생 - 코드: {}, 메시지: {}, 경로: {}", 
                    errorCode.getCode(), ex.getMessage(), requestURI);
        } else {
            log.error("[Product Service] 비즈니스 예외 발생 - 코드: {}, 메시지: {}, 경로: {}", 
                    errorCode.getCode(), ex.getMessage(), requestURI, ex);
        }
        
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, ex.getMessage(), requestURI);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
    
    /**
     * 상품 조회 실패 예외 처리
     * 
     * ProductNotFoundException을 404 응답으로 변환합니다.
     * 
     * @param ex 상품 조회 실패 예외
     * @param request HTTP 요청 정보
     * @return 에러 응답
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex, HttpServletRequest request) {
        
        String requestURI = request.getRequestURI();
        
        log.warn("[Product Service] 상품 조회 실패 - 메시지: {}, 경로: {}", ex.getMessage(), requestURI);
        
        ErrorResponse errorResponse = ErrorResponse.of(ex.getErrorCode(), ex.getMessage(), requestURI);
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
    }
    
    /**
     * 필수 요청 파라미터 누락 예외 처리
     * 
     * @param ex 파라미터 누락 예외
     * @param request HTTP 요청 정보
     * @return 에러 응답
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        String requestURI = request.getRequestURI();
        String message = String.format("필수 파라미터 '%s'가 누락되었습니다.", ex.getParameterName());
        
        log.warn("[Product Service] 필수 파라미터 누락 - 파라미터: {}, 경로: {}", ex.getParameterName(), requestURI);
        
        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.MISSING_REQUEST_PARAMETER, message, requestURI);
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * 메서드 인수 타입 불일치 예외 처리
     * 
     * @param ex 타입 불일치 예외
     * @param request HTTP 요청 정보
     * @return 에러 응답
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        String requestURI = request.getRequestURI();
        String message = String.format("파라미터 '%s'의 값 '%s'는 %s 타입으로 변환할 수 없습니다.", 
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        
        log.warn("[Product Service] 타입 변환 실패 - 파라미터: {}, 값: {}, 타입: {}, 경로: {}", 
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName(), requestURI);
        
        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.INVALID_TYPE_VALUE, message, requestURI);
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * 잘못된 인수 예외 처리
     * 
     * @param ex IllegalArgumentException
     * @param request HTTP 요청 정보
     * @return 에러 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        
        String requestURI = request.getRequestURI();
        
        log.warn("[Product Service] 잘못된 인수 - 메시지: {}, 경로: {}", ex.getMessage(), requestURI);
        
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, ex.getMessage(), requestURI);
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * 일반 런타임 예외 처리
     * 
     * 예상하지 못한 런타임 예외를 500 에러로 처리합니다.
     * 민감한 정보는 로그에만 기록하고 클라이언트에는 일반적인 메시지를 전달합니다.
     * 
     * @param ex 런타임 예외
     * @param request HTTP 요청 정보
     * @return 에러 응답
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        
        String requestURI = request.getRequestURI();
        
        log.error("[Product Service] 예상하지 못한 런타임 에러 발생 - 메시지: {}, 경로: {}, 스택트레이스: {}", 
                ex.getMessage(), requestURI, Arrays.toString(ex.getStackTrace()));
        
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, requestURI);
        return ResponseEntity.internalServerError().body(errorResponse);
    }
    
    /**
     * 일반 예외 처리
     * 
     * 모든 예외의 최종 catch-all 핸들러입니다.
     * 시스템의 안정성을 위해 예상하지 못한 모든 예외를 500 에러로 처리합니다.
     * 
     * @param ex 일반 예외
     * @param request HTTP 요청 정보
     * @return 에러 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex, HttpServletRequest request) {
        
        String requestURI = request.getRequestURI();
        
        log.error("[Product Service] 예상하지 못한 에러 발생 - 타입: {}, 메시지: {}, 경로: {}", 
                ex.getClass().getSimpleName(), ex.getMessage(), requestURI, ex);
        
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, requestURI);
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}