package com.example.sparta.order_service.application.service;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.example.sparta.order_service.domain.entity.Order;
import com.example.sparta.order_service.domain.entity.OrderClaim;
import com.example.sparta.order_service.domain.entity.OrderHistory;
import com.example.sparta.order_service.domain.repository.OrderClaimRepository;
import com.example.sparta.order_service.domain.repository.OrderRepository;
import com.example.sparta.order_service.presentation.dto.request.OrderClaimRequest;
import com.example.sparta.order_service.presentation.dto.response.OrderClaimResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderClaimCommandService {
    private final OrderClaimRepository orderClaimRepository;
    private final OrderRepository orderRepository;

    public OrderClaimResponse create(UUID orderId, UUID userId, OrderClaimRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        OrderClaim savedClaim = orderClaimRepository.save(request.toEntity());

        OrderHistory orderHistory = OrderHistory.builder()
                .reasonDetail(request.reasonDetail())
                .claimType(request.claimType())
                .reasonType(request.reasonType())
                .createdAt(LocalDateTime.now())
                .createdBy(userId)
                .build();

        order.addHistory(orderHistory);

        return savedClaim.toResponse();
    }
}
