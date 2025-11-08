package com.example.sparta.product_service.exception;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;

import java.util.UUID;

/**
 * 상품을 찾을 수 없는 경우 발생하는 예외
 * 
 * 특정 상품 ID로 조회했을 때 해당 상품이 존재하지 않거나
 * 논리적으로 삭제된 경우 발생합니다.
 * 
 * BusinessException을 상속받아 일관된 예외 처리 구조를 유지합니다.
 * 404 Not Found HTTP 상태 코드로 매핑됩니다.
 */
public class ProductNotFoundException extends BusinessException {
    
    /**
     * 상품 ID와 함께 예외를 생성합니다.
     * 
     * @param productId 찾을 수 없는 상품의 ID
     */
    public ProductNotFoundException(UUID productId) {
        super(ErrorCode.PRODUCT_NOT_FOUND, String.format("상품을 찾을 수 없습니다. ID: %s", productId));
    }
    
    /**
     * 커스텀 메시지와 함께 예외를 생성합니다.
     * 
     * @param message 사용자 정의 오류 메시지
     */
    public ProductNotFoundException(String message) {
        super(ErrorCode.PRODUCT_NOT_FOUND, message);
    }
    
    /**
     * 상품 ID와 원인 예외와 함께 예외를 생성합니다.
     * 
     * @param productId 찾을 수 없는 상품의 ID
     * @param cause 원인 예외
     */
    public ProductNotFoundException(UUID productId, Throwable cause) {
        super(ErrorCode.PRODUCT_NOT_FOUND, String.format("상품을 찾을 수 없습니다. ID: %s", productId), cause);
    }
}