package com.example.sparta.order_service.application.dto.message;

import java.util.UUID;

public record DeliveryCompleteMessage(
        UUID deliveryId,
        UUID orderId
) {
}
