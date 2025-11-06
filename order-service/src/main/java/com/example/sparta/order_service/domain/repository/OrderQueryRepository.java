package com.example.sparta.order_service.domain.repository;

import com.example.sparta.order_service.domain.entity.Order;
import com.example.sparta.order_service.presentation.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderQueryRepository {
    Page<OrderResponse> findAll(Pageable pageable);
    Page<OrderResponse> findAllByUserEmail(String userEmail, Pageable pageable);
}
