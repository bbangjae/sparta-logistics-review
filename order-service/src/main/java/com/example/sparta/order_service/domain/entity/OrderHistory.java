package com.example.sparta.order_service.domain.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistory {
    private String reasonDetail;
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;
    @Enumerated(EnumType.STRING)
    private ClaimType claimType;
    @Enumerated(EnumType.STRING)
    private ReasonType reasonType;
    private LocalDateTime createdAt;
    private UUID createdBy;

    @Builder
    public OrderHistory(String reasonDetail, ClaimStatus status, ClaimType claimType, ReasonType reasonType, LocalDateTime createdAt, UUID createdBy) {
        this.reasonDetail = reasonDetail;
        this.status = status;
        this.claimType = claimType;
        this.reasonType = reasonType;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}
