package com.sparta.deliveryservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCancelEvent {
    private String correlationId;
    private UUID orderId;
    private String reason;
}
