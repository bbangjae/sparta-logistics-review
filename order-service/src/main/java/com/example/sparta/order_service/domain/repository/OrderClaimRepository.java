package com.example.sparta.order_service.domain.repository;

import com.example.sparta.order_service.domain.entity.OrderClaim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderClaimRepository extends JpaRepository<OrderClaim, UUID> {
    Optional<OrderClaim> findByOrderId(UUID orderId);
}
