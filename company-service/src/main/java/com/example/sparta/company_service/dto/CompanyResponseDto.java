package com.example.sparta.company_service.dto;

import com.example.sparta.company_service.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 업체 정보 응답 DTO
 * 
 * 클라이언트에게 업체 정보를 전달하기 위한 데이터 전송 객체입니다.
 * API 명세에 정의된 JSON 응답 형식에 맞추어 설계되었으며,
 * 내부 도메인 모델(Entity)과 외부 API의 결합도를 낮춥니다. (DIP - 의존성 역전 원칙)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponseDto {
    
    /**
     * 업체 고유 식별자
     */
    private UUID company_id;
    
    /**
     * 업체명 (예: "롯데택배")
     */
    private String name;
    
    /**
     * 업체 유형 표시명 (예: "생산업체", "배송업체")
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
     * 업체 상태 (ACTIVE, INACTIVE, DELETED)
     */
    private String status;
    
    /**
     * 업체 생성일시
     */
    private LocalDateTime created_at;
    
    /**
     * Entity를 DTO로 변환하는 정적 팩토리 메서드
     * 
     * 도메인 모델을 API 응답 형식으로 변환하며,
     * 변환 로직을 한 곳에 집중시켜 유지보수성을 높입니다. (SRP - 단일 책임 원칙)
     * 
     * @param company 변환할 Company Entity
     * @return 변환된 CompanyResponseDto
     */
    public static CompanyResponseDto from(Company company) {
        return CompanyResponseDto.builder()
                .company_id(company.getCompanyId())
                .name(company.getName())
                .type(getTypeDisplayName(company.getType()))
                .hub_id(company.getHubId())
                .address(company.getAddress())
                .status(company.getStatus().name())
                .created_at(company.getCreatedAt())
                .build();
    }
    
    /**
     * 업체 유형을 사용자 친화적인 표시명으로 변환
     * 
     * 내부 enum 값을 클라이언트가 이해하기 쉬운 한글 표시명으로 변환합니다.
     * 새로운 업체 유형이 추가되어도 이 메서드만 수정하면 됩니다. (OCP - 개방-폐쇄 원칙)
     * 
     * @param type 업체 유형 enum
     * @return 한글 표시명
     */
    private static String getTypeDisplayName(Company.CompanyType type) {
        return switch (type) {
            case PRODUCTION -> "생산업체";
            case RECEPTION -> "배송업체";
        };
    }
}