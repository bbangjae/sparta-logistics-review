package com.example.sparta.hub_route_service.infrastructure.client;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service", path = "/hubs")
public interface HubClient {
    @GetMapping("/{hubId}/exists")
    boolean existsHub(@PathVariable("hubId") UUID hubId);

    /**
     * hub-service로부터 전체 허브 목록 조회
     *
     * @return 허브 상세 정보 리스트
     */
    @GetMapping
    List<HubDetailResponse> getAllHubs();
}
