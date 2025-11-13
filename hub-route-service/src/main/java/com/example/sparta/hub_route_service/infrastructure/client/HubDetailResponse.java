package com.example.sparta.hub_route_service.infrastructure.client;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * hub-service로부터 받는 허브 상세 정보 DTO
 * hub-service의 HubDetailResponse와 동일한 구조
 */
public record HubDetailResponse(
    UUID hubId,
    String code,
    String name,
    String address,
    String status,
    BigDecimal latitude,
    BigDecimal longitude
) {}
