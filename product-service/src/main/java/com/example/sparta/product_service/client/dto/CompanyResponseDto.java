package com.example.sparta.product_service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Company Service로부터 받는 업체 응답 DTO
 * 
 * Company Service의 API 응답을 매핑하기 위한 DTO입니다.
 * 서비스 간 통신에서 필요한 최소한의 정보만 포함하여 결합도를 낮췄습니다.
 * 
 * Company Service의 응답 형식과 동일하게 유지해야 합니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponseDto {
    
    /**
     * 업체 고유 식별자
     */
    private UUID company_id;
    
    /**
     * 업체명
     */
    private String name;
    
    /**
     * 업체 유형 (생산업체/수령업체)
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
     * 업체 상태 (ACTIVE/INACTIVE)
     */
    private String status;
    
    /**
     * 생성일시
     */
    private LocalDateTime created_at;
    
    /**
     * 생성자 ID
     */
    private Long created_by;
    
    /**
     * 수정일시
     */
    private LocalDateTime updated_at;
    
    /**
     * 수정자 ID
     */
    private Long updated_by;
    
    /**
     * 업체가 활성 상태인지 확인
     * 
     * @return 활성 상태면 true, 비활성 상태면 false
     */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
}