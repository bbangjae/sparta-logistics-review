package com.example.sparta.order_service.domain.repository;

import com.example.sparta.order_service.domain.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderLineRepository extends JpaRepository<OrderLine, UUID> {
}
