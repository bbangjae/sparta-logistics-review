package com.example.sparta.order_service.domain.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @Builder
    public OrderHistory(String reasonDetail, ClaimStatus status, ClaimType claimType, ReasonType reasonType, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        this.reasonDetail = reasonDetail;
        this.status = status;
        this.claimType = claimType;
        this.reasonType = reasonType;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
