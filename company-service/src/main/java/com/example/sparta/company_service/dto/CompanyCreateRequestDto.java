package com.example.sparta.company_service.dto;

import com.example.sparta.company_service.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 업체 생성 요청 DTO
 * 
 * 클라이언트로부터 업체 생성 정보를 받기 위한 데이터 전송 객체입니다.
 * 입력값 검증과 Entity 변환 로직을 포함합니다.
 * 단일 책임 원칙(SRP)에 따라 요청 데이터 처리만 담당합니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateRequestDto {
    
    /**
     * 업체명 (필수)
     */
    private String name;
    
    /**
     * 업체 유형 (예: "물류", "배송업체")
     */
    private String type;
    
    /**
     * 소속 허브 ID (필수)
     */
    private UUID hub_id;
    
    /**
     * 업체 주소 (필수)
     */
    private String address;
    
    /**
     * 입력값 검증
     * 
     * 비즈니스 규칙에 따른 필수값 및 형식 검증을 수행합니다.
     * 단일 책임 원칙(SRP)에 따라 검증 로직만 담당합니다.
     * 
     * @throws IllegalArgumentException 검증 실패 시
     */
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("업체명은 필수입니다.");
        }
        
        if (name.trim().length() > 100) {
            throw new IllegalArgumentException("업체명은 100자를 초과할 수 없습니다.");
        }
        
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("업체 유형은 필수입니다.");
        }
        
        if (hub_id == null) {
            throw new IllegalArgumentException("허브 ID는 필수입니다.");
        }
        
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("주소는 필수입니다.");
        }
        
        if (address.trim().length() > 500) {
            throw new IllegalArgumentException("주소는 500자를 초과할 수 없습니다.");
        }
    }
    
    /**
     * 업체 유형 문자열을 CompanyType enum으로 변환
     * 
     * API 명세의 "물류" 같은 문자열을 내부 enum으로 매핑합니다.
     * 개방-폐쇄 원칙(OCP)에 따라 새로운 타입 추가 시 확장 가능합니다.
     * 
     * @return CompanyType enum
     * @throws IllegalArgumentException 지원하지 않는 타입인 경우
     */
    public Company.CompanyType parseCompanyType() {
        if (type == null) {
            throw new IllegalArgumentException("업체 유형이 없습니다.");
        }
        
        String normalizedType = type.trim();
        
        // API 명세에 따른 타입 매핑
        return switch (normalizedType) {
            case "물류", "배송업체", "RECEPTION" -> Company.CompanyType.RECEPTION;
            case "생산업체", "PRODUCTION" -> Company.CompanyType.PRODUCTION;
            default -> throw new IllegalArgumentException(
                    String.format("지원하지 않는 업체 유형입니다: %s. 지원 타입: 물류, 배송업체, 생산업체", normalizedType)
            );
        };
    }
    
    /**
     * DTO를 Entity로 변환하는 팩토리 메서드
     * 
     * 요청 데이터를 도메인 모델로 변환하며,
     * 생성 시점의 기본값들을 설정합니다.
     * 
     * @return 변환된 Company Entity
     */
    public Company toEntity() {
        return Company.builder()
                .name(this.name.trim())
                .type(this.parseCompanyType())
                .hubId(this.hub_id)
                .address(this.address.trim())
                .status(Company.CompanyStatus.ACTIVE) // 기본값: 활성 상태
                .build();
    }
}