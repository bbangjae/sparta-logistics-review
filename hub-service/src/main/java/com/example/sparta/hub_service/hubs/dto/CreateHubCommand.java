package com.example.sparta.hub_service.hubs.dto;

import java.math.BigDecimal;

public record CreateHubCommand(
    String code,
    String name,
    String address,
    BigDecimal latitude,
    BigDecimal longitude
) {
}
