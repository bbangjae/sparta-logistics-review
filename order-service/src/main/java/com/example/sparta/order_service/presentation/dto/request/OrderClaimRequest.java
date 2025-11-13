package com.example.sparta.order_service.presentation.dto.request;

import com.example.sparta.order_service.domain.entity.ClaimStatus;
import com.example.sparta.order_service.domain.entity.ClaimType;
import com.example.sparta.order_service.domain.entity.OrderClaim;
import com.example.sparta.order_service.domain.entity.ReasonType;
import lombok.Builder;

@Builder
public record OrderClaimRequest(
        ClaimType claimType,
        ClaimStatus status,
        String reasonDetail,
        ReasonType reasonType) {

    public OrderClaim toEntity() {
        return OrderClaim.builder()
                .claimType(claimType)
                .status(status)
                .reasonDetail(reasonDetail)
                .build();
    }
}
