package com.example.sparta.order_service.domain.entity;

import com.example.sparta.order_service.presentation.dto.response.OrderClaimResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

// TODO 필수 기능 구현 이후 클레임 기능을 통해 배송지 변경 등의 요청 기능 구현
@Entity
@Table(name = "p_order_claims")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderClaimId;
    @Column(nullable = false)
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimType claimType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;
    @Column(nullable = false)
    private String reasonDetail;

    @Builder
    public OrderClaim(UUID orderClaimId, ClaimType claimType, ClaimStatus status, String reasonDetail, UUID orderId) {
        this.orderClaimId = orderClaimId;
        this.claimType = claimType;
        this.status = status;
        this.reasonDetail = reasonDetail;
        this.orderId = orderId;
    }

    public OrderClaimResponse toResponse() {
        return OrderClaimResponse.builder()
                .orderClaimId(orderClaimId)
                .orderId(orderId)
                .claimType(claimType)
                .status(status)
                .reasonDetail(reasonDetail)
                .build();
    }
}
