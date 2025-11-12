package com.example.sparta.order_service.domain.repository;

import com.example.sparta.order_service.domain.entity.OrderClaim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderClaimRepository extends JpaRepository<OrderClaim, UUID> {
    Page<OrderClaim> findAllByOrderId(UUID orderId, Pageable pageable);
}
