package com.example.sparta.order_service.presentation.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderLineResponse(String productName,
                                Long price,
                                Integer quantity,
                                Long amount,
                                UUID productId) {
}
