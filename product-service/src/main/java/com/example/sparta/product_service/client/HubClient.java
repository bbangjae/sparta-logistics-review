package com.example.sparta.product_service.client;

import com.example.sparta.product_service.client.dto.HubResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * Hub 서비스와의 통신을 위한 FeignClient
 * 
 * Hub 서비스의 API를 호출하여 허브 정보를 조회합니다.
 * MSA 환경에서 서비스 간 통신을 담당하며,
 * 회로 차단기 패턴을 통해 장애 전파를 방지합니다.
 */
@FeignClient(name = "hub-service", path = "/hubs")
public interface HubClient {
    
    /**
     * 특정 허브 정보를 조회합니다.
     * 
     * @param hubId 조회할 허브 ID
     * @return 허브 상세 정보
     * @throws feign.FeignException.NotFound 허브가 존재하지 않는 경우
     */
    @GetMapping("/{hubId}")
    HubResponseDto getHub(@PathVariable("hubId") UUID hubId);
}