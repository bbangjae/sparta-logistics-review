package com.example.sparta.order_service.presentation.dto.request;

import com.example.sparta.order_service.domain.entity.OrderLine;

import java.util.UUID;

public record OrderLineRequest(String productName,
                               Long price,
                               Integer quantity,
                               UUID productId) {

    public OrderLine toEntity() {
        return OrderLine.builder()
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .amounts(price * quantity)
                .build();
    }
}
