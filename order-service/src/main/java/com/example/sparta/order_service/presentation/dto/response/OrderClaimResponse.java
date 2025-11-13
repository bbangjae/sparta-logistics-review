package com.example.sparta.order_service.presentation.dto.response;

import com.example.sparta.order_service.domain.entity.ClaimStatus;
import com.example.sparta.order_service.domain.entity.ClaimType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderClaimResponse(
        UUID orderClaimId,
        UUID orderId,
        ClaimType claimType,
        ClaimStatus status,
        String reasonDetail
) {
}
