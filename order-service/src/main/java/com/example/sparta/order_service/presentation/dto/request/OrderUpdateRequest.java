package com.example.sparta.order_service.presentation.dto.request;

import com.example.sparta.order_service.domain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderUpdateRequest(
        String userEmail,
        OrderStatus status,
        String deliveryMessage,
        LocalDateTime dueDate,
        ShippingInfoRequest originInfo,
        ShippingInfoRequest recipientInfo,
        List<OrderLineRequest> orderLines) {
}
