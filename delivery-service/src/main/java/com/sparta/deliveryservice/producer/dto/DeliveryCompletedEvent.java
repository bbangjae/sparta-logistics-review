package com.sparta.deliveryservice.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DeliveryCompletedEvent {
    // D. 주문 서비스에 전달할 최소한의 정보
    private UUID deliveryId;
    private UUID orderId;
    private LocalDateTime completedAt;
}
