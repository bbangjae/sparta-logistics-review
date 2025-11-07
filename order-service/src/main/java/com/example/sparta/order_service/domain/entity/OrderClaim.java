package com.example.sparta.order_service.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimType claimType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;
    private String reasonDetail;
}
