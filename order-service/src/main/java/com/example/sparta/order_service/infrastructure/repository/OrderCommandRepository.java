package com.example.sparta.order_service.infrastructure.repository;

import com.example.sparta.order_service.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderCommandRepository extends JpaRepository<Order, UUID> {
}
