package com.sparta.deliveryservice.producer.dto;

import com.sparta.deliveryservice.domain.dto.request.DeliveryCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Value
@Getter
@AllArgsConstructor
public class OrderCreatedEvent {

    // 배송 서비스에 전달할 최소한의 정보
    private UUID orderId;
    private String destinationAddress;
    private String recipientName;
    private String recipientSlackId;
    private UUID originHubId;
    private UUID destinationHubId;
    private List<ItemInfo> items;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfo {
        private UUID itemId;
        private int quantity;
    }
}

