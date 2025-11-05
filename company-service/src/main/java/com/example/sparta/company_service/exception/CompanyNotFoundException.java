package com.example.sparta.company_service.exception;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;

import java.util.UUID;

/**
 * 업체를 찾을 수 없을 때 발생하는 예외
 * 
 * common 모듈의 BusinessException을 상속받아 일관된 예외 처리를 제공합니다.
 * 단일 책임 원칙(SRP)에 따라 업체 조회 실패 예외 표현만 담당합니다.
 * 리스코프 치환 원칙(LSP)을 준수하여 BusinessException을 완전히 대체할 수 있습니다.
 */
public class CompanyNotFoundException extends BusinessException {
    
    /**
     * 업체 ID로 조회 실패 예외 생성
     * 
     * @param companyId 찾을 수 없는 업체 ID
     */
    public CompanyNotFoundException(UUID companyId) {
        super(ErrorCode.COMPANY_NOT_FOUND, "업체 ID: " + companyId);
    }
    
    /**
     * 커스텀 메시지와 함께 업체 조회 실패 예외 생성
     * 
     * @param message 커스텀 메시지
     */
    public CompanyNotFoundException(String message) {
        super(ErrorCode.COMPANY_NOT_FOUND, message);
    }
}