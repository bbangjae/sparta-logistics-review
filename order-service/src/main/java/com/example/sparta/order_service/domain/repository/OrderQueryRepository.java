package com.example.sparta.order_service.domain.repository;

import com.example.sparta.order_service.domain.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderQueryRepository {
    Optional<List<Order>> findAllByUserEmail(String userEmail);
}
