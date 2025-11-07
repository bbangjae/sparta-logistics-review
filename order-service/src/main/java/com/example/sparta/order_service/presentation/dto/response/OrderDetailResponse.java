package com.example.sparta.order_service.presentation.dto.response;

import com.example.sparta.order_service.domain.entity.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderDetailResponse(String deliveryMessage,
                                  Long totalAmount,
                                  LocalDateTime orderDate,
                                  String orderedBy,
                                  OrderStatus state,
                                  ShippingInfoResponse originInfo,
                                  ShippingInfoResponse recipientInfo,
                                  List<OrderLineResponse> orderLines) {
}