package com.example.sparta.order_service.application.dto.message;

import com.example.sparta.order_service.application.dto.request.DeliveryCreateRequest;
import com.example.sparta.order_service.domain.entity.ShippingInfo;

import java.util.UUID;

public record OrderCreatedMessage(
        DeliveryCreateRequest deliveryCreateRequest
) {
}
