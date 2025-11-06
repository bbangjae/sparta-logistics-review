package com.example.sparta.order_service.presentation.dto.request;

import com.example.sparta.order_service.domain.entity.Order;
import com.example.sparta.order_service.domain.entity.OrderLine;
import com.example.sparta.order_service.domain.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequest(
        @NotNull
        String deliveryMessage,
        @NotNull LocalDateTime dueDate,
        @NotNull ShippingInfoRequest originInfo,
        @NotNull ShippingInfoRequest recipientInfo,
        @NotNull List<OrderLineRequest> orderLines) {

    public Order toEntity() {
        List<OrderLine> orderLineEntities = orderLines.stream()
                .map(OrderLineRequest::toEntity)
                .toList();
        long totalAmount = orderLineEntities.stream()
                .mapToLong(OrderLine::getAmounts)
                .sum();

        Order order = Order.builder()
                .deliveryMessage(deliveryMessage)
                .originInfo(originInfo.toEntity())
                .recipientInfo(recipientInfo.toEntity())
                .orderLines(orderLineEntities)
                .status(OrderStatus.PAYMENT_PENDING)
                .totalAmount(totalAmount)
                .build();

        orderLineEntities.forEach(orderLine -> orderLine.setOrderToCreate(order));

        return order;
    }
}
