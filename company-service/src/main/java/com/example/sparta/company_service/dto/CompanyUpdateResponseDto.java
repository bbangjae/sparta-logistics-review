package com.example.sparta.company_service.dto;

import com.example.sparta.company_service.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 업체 정보 수정 응답 DTO
 * 
 * 업체 수정 성공 시 반환하는 데이터 전송 객체입니다.
 * API 명세에 정의된 수정 응답 형식에 맞추어 설계되었으며,
 * 수정 시점의 특정 정보만 포함합니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateResponseDto {
    
    /**
     * 수정된 업체 고유 식별자
     */
    private UUID company_id;
    
    /**
     * 수정된 업체명
     */
    private String name;
    
    /**
     * 수정자 ID
     */
    private Long updated_by;
    
    /**
     * 수정일시
     */
    private LocalDateTime updated_at;
    
    /**
     * Entity를 수정 응답 DTO로 변환하는 정적 팩토리 메서드
     * 
     * 수정된 업체 Entity를 API 명세에 맞는 응답 형식으로 변환합니다.
     * 단일 책임 원칙(SRP)에 따라 변환 로직만 담당합니다.
     * 
     * @param company 변환할 Company Entity
     * @return 변환된 CompanyUpdateResponseDto
     */
    public static CompanyUpdateResponseDto from(Company company) {
        return CompanyUpdateResponseDto.builder()
                .company_id(company.getCompanyId())
                .name(company.getName())
                .updated_by(company.getUpdatedBy())
                .updated_at(company.getUpdatedAt())
                .build();
    }
}