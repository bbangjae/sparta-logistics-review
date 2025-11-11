package com.example.sparta.order_service.application.dto.message;

import java.util.UUID;

public record DeliveryCreatedMessage(
        UUID orderId,
        UUID deliveryId,
        UUID originHubId,
        UUID destinationHubId
) {
}
