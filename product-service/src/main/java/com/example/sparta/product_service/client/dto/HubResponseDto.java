package com.example.sparta.product_service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Hub 서비스로부터 받는 허브 정보 응답 DTO
 * 
 * Hub 서비스의 HubDetailResponse와 호환되도록 설계되었으며,
 * Product 서비스에서 허브 검증에 필요한 정보만 포함합니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubResponseDto {
    
    /**
     * 허브 고유 식별자
     */
    private UUID hubId;
    
    /**
     * 허브 코드
     */
    private String code;
    
    /**
     * 허브명
     */
    private String name;
    
    /**
     * 허브 주소 정보
     */
    private String address;
    
    /**
     * 허브 운영 상태 (ACTIVE: 정상 운영, CLOSED: 운영 종료)
     */
    private String status;
    
    /**
     * 위도
     */
    private BigDecimal latitude;
    
    /**
     * 경도
     */
    private BigDecimal longitude;
    
    /**
     * 허브가 활성 상태인지 확인
     * 
     * @return 활성 상태면 true, 비활성이면 false
     */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
}