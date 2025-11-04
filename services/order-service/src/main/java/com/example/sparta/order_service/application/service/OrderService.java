package com.example.sparta.order_service.application.service;

import com.example.sparta.order_service.domain.entity.Order;
import com.example.sparta.order_service.domain.repository.OrderRepository;
import com.example.sparta.order_service.presentation.dto.request.OrderRequest;
import com.example.sparta.order_service.presentation.dto.response.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderCreateResponse create(OrderRequest request) {
        return orderRepository.save(request.toEntity()).toCreateResponse();
    }
}
