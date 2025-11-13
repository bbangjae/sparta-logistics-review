package com.sparta.deliveryservice.domain.dto.request;

import com.sparta.deliveryservice.producer.dto.DeliveryCreatedEvent;
import com.sparta.deliveryservice.producer.dto.OrderCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
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

    // event를 받아서 DeliveryCreateRequest 객체로 변환하는 정적 메서드
    public static DeliveryCreateRequest from(OrderCreatedEvent event) {
        DeliveryCreateRequest request = new DeliveryCreateRequest();
        request.setOrderId(event.getOrderId());
        request.setDestinationAddress(event.getDestinationAddress());
        request.setRecipientName(event.getRecipientName());
        request.setRecipientSlackId(event.getRecipientSlackId());
        request.setOriginHubId(event.getOriginHubId());
        request.setDestinationHubId(event.getDestinationHubId());
        request.setItems(event.getItems());
        return request;
    }

    private void setItems(List<OrderCreatedEvent.ItemInfo> items) {
        this.items = new ArrayList<>();
        for (OrderCreatedEvent.ItemInfo item : items) {
            this.items.add(new ItemInfo(item.getItemId(), item.getQuantity()));
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfo {
        private UUID itemId;
        private int quantity;
    }

}
