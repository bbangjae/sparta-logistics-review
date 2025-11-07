package com.sparta.deliveryservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RouteInfoResponse {
    private UUID originHubId;
    private UUID destinationHubId;
    private Double estimatedDistance;
    private Integer estimatedDuration;

    // 테스트 코드의 호출을 위해 임시 생성자 추가
    // 나중에 B서비스 스펙에 맞춰 수정
    public RouteInfoResponse(UUID originHubId, UUID destinationHubId) {
        this.originHubId = originHubId;
        this.destinationHubId = destinationHubId;
    }
}
