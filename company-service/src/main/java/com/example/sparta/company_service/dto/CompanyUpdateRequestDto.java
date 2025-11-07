package com.example.sparta.company_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 업체 정보 수정 요청 DTO
 * 
 * 클라이언트로부터 업체 수정 정보를 받기 위한 데이터 전송 객체입니다.
 * 입력값 검증 로직을 포함하며, 부분 업데이트를 지원합니다.
 * 단일 책임 원칙(SRP)에 따라 요청 데이터 처리만 담당합니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateRequestDto {
    
    /**
     * 수정할 업체명 (선택적)
     */
    private String name;
    
    /**
     * 수정할 업체 주소 (선택적)
     */
    private String address;
    
    /**
     * 입력값 검증
     * 
     * 비즈니스 규칙에 따른 필수값 및 형식 검증을 수행합니다.
     * 부분 업데이트이므로 null 값은 허용되지만, 빈 문자열은 검증합니다.
     * 
     * @throws IllegalArgumentException 검증 실패 시
     */
    public void validate() {
        // 업체명 검증 (null은 허용, 빈 문자열은 불허)
        if (name != null) {
            if (name.trim().isEmpty()) {
                throw new IllegalArgumentException("업체명은 빈 값일 수 없습니다.");
            }
            
            if (name.trim().length() > 100) {
                throw new IllegalArgumentException("업체명은 100자를 초과할 수 없습니다.");
            }
        }
        
        // 주소 검증 (null은 허용, 빈 문자열은 불허)
        if (address != null) {
            if (address.trim().isEmpty()) {
                throw new IllegalArgumentException("주소는 빈 값일 수 없습니다.");
            }
            
            if (address.trim().length() > 500) {
                throw new IllegalArgumentException("주소는 500자를 초과할 수 없습니다.");
            }
        }
    }
    
    /**
     * 수정할 필드가 있는지 확인
     * 
     * @return 수정할 필드가 하나라도 있으면 true, 모두 null이면 false
     */
    public boolean hasFieldsToUpdate() {
        return name != null || address != null;
    }
    
    /**
     * 업체명이 수정 대상인지 확인
     * 
     * @return 업체명 수정 여부
     */
    public boolean isNameToUpdate() {
        return name != null;
    }
    
    /**
     * 주소가 수정 대상인지 확인
     * 
     * @return 주소 수정 여부
     */
    public boolean isAddressToUpdate() {
        return address != null;
    }
    
    /**
     * 정규화된 업체명 반환
     * 
     * @return 공백이 제거된 업체명
     */
    public String getNormalizedName() {
        return name != null ? name.trim() : null;
    }
    
    /**
     * 정규화된 주소 반환
     * 
     * @return 공백이 제거된 주소
     */
    public String getNormalizedAddress() {
        return address != null ? address.trim() : null;
    }
}