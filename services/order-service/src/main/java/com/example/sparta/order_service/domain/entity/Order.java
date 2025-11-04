package com.example.sparta.order_service.domain.entity;

import com.example.sparta.order_service.presentation.dto.response.OrderCreateResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    @Column(nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private Long totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String deliveryMessage;
    private Integer deliveryFee;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "companyName", column = @Column(name = "origin_company_name", nullable = false, length = 100)),
            @AttributeOverride(name = "name", column = @Column(name = "origin_name", nullable = false, length = 10)),
            @AttributeOverride(name = "phone", column = @Column(name = "origin_phone", nullable = false, length = 13)),
            @AttributeOverride(name = "address", column = @Column(name = "origin_address", nullable = false, length = 50)),
            @AttributeOverride(name = "addressDetail", column = @Column(name = "origin_address_detail", nullable = false, length = 100)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "origin_zip_code", nullable = false, length = 5))
    })
    private ShippingInfo originInfo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "companyName", column = @Column(name = "recipient_company_name", nullable = false, length = 100)),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name", nullable = false, length = 10)),
            @AttributeOverride(name = "phone", column = @Column(name = "recipient_phone", nullable = false, length = 13)),
            @AttributeOverride(name = "address", column = @Column(name = "recipient_address", nullable = false, length = 50)),
            @AttributeOverride(name = "addressDetail", column = @Column(name = "recipient_address_detail", nullable = false, length = 100)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zip_code", nullable = false, length = 5))
    })
    private ShippingInfo recipientInfo;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "p_order_histories",
            joinColumns = @JoinColumn(name = "order_id")
    )
    @OrderBy("createdAt DESC")
    private List<OrderHistory> orderHistories = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @LastModifiedBy
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;

    @Builder
    public Order(UUID orderId, String userEmail, Long totalAmount, OrderStatus status, String deliveryMessage, ShippingInfo originInfo, ShippingInfo recipientInfo, List<OrderLine> orderLines, List<OrderHistory> orderHistories, Integer deliveryFee) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.totalAmount = totalAmount;
        this.status = status;
        this.deliveryMessage = deliveryMessage;
        this.originInfo = originInfo;
        this.recipientInfo = recipientInfo;
        this.orderLines = orderLines;
        this.orderHistories = orderHistories;
        this.deliveryFee = deliveryFee;
    }

    public OrderCreateResponse toCreateResponse() {
        return OrderCreateResponse.builder()
                .deliveryMessage(deliveryMessage)
                .totalAmount(totalAmount)
                .orderDate(createdAt)
                .orderedBy(createdBy)
                .state(status)
                .originInfo(originInfo.toResponse())
                .recipientInfo(recipientInfo.toResponse())
                .orderLines(orderLines.stream().map(OrderLine::toResponse).toList())
                .build();
    }

    public void setUserEmailToCreate(String email) {
        userEmail = email;
    }
}
