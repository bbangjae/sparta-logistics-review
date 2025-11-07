package com.example.sparta.company_service.dto;

import com.example.sparta.company_service.entity.Company;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * 업체 검색 조건을 캡슐화하는 DTO
 * 
 * 검색 파라미터들을 하나의 객체로 묶어 관리하며,
 * 비즈니스 로직과 검색 조건의 의존성을 줄입니다. (SRP - 단일 책임 원칙)
 */
@Getter
@Builder
public class CompanySearchCriteria {
    
    /**
     * 업체명 검색 키워드 (부분 일치)
     */
    private final String name;
    
    /**
     * 허브 ID로 필터링
     */
    private final UUID hubId;
    
    /**
     * 업체 상태로 필터링
     */
    private final Company.CompanyStatus status;
    
    /**
     * 문자열 상태값을 CompanyStatus enum으로 변환
     * 
     * @param statusString 상태 문자열 ("ACTIVE", "INACTIVE", "DELETED")
     * @return 변환된 CompanyStatus 또는 null (잘못된 값인 경우)
     */
    public static Company.CompanyStatus parseStatus(String statusString) {
        if (statusString == null) {
            return null;
        }
        
        try {
            return Company.CompanyStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 잘못된 status 값이면 null로 처리 (모든 상태 조회)
            return null;
        }
    }
}