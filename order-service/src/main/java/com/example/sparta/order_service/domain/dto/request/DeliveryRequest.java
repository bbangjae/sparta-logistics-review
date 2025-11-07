package com.example.sparta.order_service.domain.dto.request;

import com.example.sparta.order_service.presentation.dto.request.OrderLineRequest;
import com.example.sparta.order_service.presentation.dto.request.ShippingInfoRequest;

import java.util.List;
import java.util.UUID;

public record DeliveryRequest(
        UUID orderId,
        UUID originHubId,
        UUID destinationHubId,
        ShippingInfoRequest recipientInfo,
        List<OrderLineRequest> orderLines
) {
}
