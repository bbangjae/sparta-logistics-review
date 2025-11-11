package com.example.sparta.order_service.application.dto.message;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderCreatedMessage(
        UUID orderId,
        String originAddress,
        String destinationAddress
) {
}
