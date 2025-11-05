package com.example.sparta.order_service.presentation.dto.request;

import com.example.sparta.order_service.domain.entity.OrderLine;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record OrderLineRequest(
        @NotNull
        String productName,
        @NotNull
        @Positive
        Long price,
        @NotNull
        @Positive
        Integer quantity,
        @NotNull
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
