package com.example.sparta.company_service.dto;

import com.example.sparta.company_service.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 업체 생성 응답 DTO
 * 
 * 업체 생성 성공 시 반환하는 데이터 전송 객체입니다.
 * API 명세에 정의된 생성 응답 형식에 맞추어 설계되었으며,
 * CompanyResponseDto와 구분하여 생성 시점의 특정 정보만 포함합니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateResponseDto {
    
    /**
     * 생성된 업체 고유 식별자
     */
    private UUID company_id;
    
    /**
     * 업체명
     */
    private String name;
    
    /**
     * 업체 유형 표시명 (예: "물류", "생산업체")
     */
    private String type;
    
    /**
     * 소속 허브 ID
     */
    private UUID hub_id;
    
    /**
     * 업체 주소
     */
    private String address;
    
    /**
     * 업체 상태 (기본값: ACTIVE)
     */
    private String status;
    
    /**
     * 생성자 ID
     */
    private Long created_by;
    
    /**
     * 생성일시
     */
    private LocalDateTime created_at;
    
    /**
     * Entity를 생성 응답 DTO로 변환하는 정적 팩토리 메서드
     * 
     * 생성된 업체 Entity를 API 명세에 맞는 응답 형식으로 변환합니다.
     * 단일 책임 원칙(SRP)에 따라 변환 로직만 담당합니다.
     * 
     * @param company 변환할 Company Entity
     * @return 변환된 CompanyCreateResponseDto
     */
    public static CompanyCreateResponseDto from(Company company) {
        return CompanyCreateResponseDto.builder()
                .company_id(company.getCompanyId())
                .name(company.getName())
                .type(getTypeDisplayName(company.getType()))
                .hub_id(company.getHubId())
                .address(company.getAddress())
                .status(company.getStatus().name())
                .created_by(company.getCreatedBy())
                .created_at(company.getCreatedAt())
                .build();
    }
    
    /**
     * 업체 유형을 API 명세에 맞는 표시명으로 변환
     * 
     * 내부 enum 값을 클라이언트가 이해하기 쉬운 표시명으로 변환합니다.
     * 개방-폐쇄 원칙(OCP)에 따라 새로운 타입 추가 시 확장 가능합니다.
     * 
     * @param type 업체 유형 enum
     * @return API 명세에 따른 표시명
     */
    private static String getTypeDisplayName(Company.CompanyType type) {
        return switch (type) {
            case PRODUCTION -> "생산업체";
            case RECEPTION -> "물류";
        };
    }
}