package com.example.sparta.order_service.domain.entity;

import com.example.sparta.order_service.presentation.dto.response.OrderLineResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_order_lines")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderLineId;
    @Column(nullable = false, length = 50)
    private String productName;
    @Column(nullable = false)
    private Long price;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    @Getter
    private Long amounts;
    @Column(nullable = false)
    private UUID productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public OrderLine(UUID orderLineId, String productName, Long price, Integer quantity, Long amounts, UUID productId, Order order) {
        this.orderLineId = orderLineId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.amounts = amounts;
        this.productId = productId;
        this.order = order;
    }

    public OrderLineResponse toResponse() {
        return OrderLineResponse.builder()
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .amount(amounts)
                .build();
    }

    public void setOrderToCreate(Order order) {
        this.order = order;
    }
}
