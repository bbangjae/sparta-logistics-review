package com.sparta.deliveryservice.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCreateRequest {

    // 테스트의 new DliveryCreateRequest() 호출을 만족시키는 필드
    private UUID orderId;
    private String destinationAddress;
    private String recipientName;
    private String recipientSlackId;
    private UUID originHubId;
    private UUID destinationHubId;
    private List<ItemInfo> items; // ItemInfo는 DTO 내부 클래스로 임시 정의

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfo {
        private UUID itemId;
        private int quantity;
    }
}
