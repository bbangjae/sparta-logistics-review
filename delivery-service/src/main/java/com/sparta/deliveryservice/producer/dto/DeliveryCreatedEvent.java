package com.sparta.deliveryservice.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Getter
@AllArgsConstructor
public class DeliveryCreatedEvent {

    // D. 주문 서비스에 전달할 최소한의 정보
    private UUID deliveryId;
    private UUID orderId;
    private UUID originHubId;
    private UUID destinationHubId;
}

