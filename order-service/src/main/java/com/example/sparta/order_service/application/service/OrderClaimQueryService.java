package com.example.sparta.order_service.application.service;

import com.example.sparta.order_service.domain.entity.OrderClaim;
import com.example.sparta.order_service.domain.repository.OrderClaimRepository;
import com.example.sparta.order_service.presentation.dto.response.OrderClaimResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderClaimQueryService {
    private final OrderClaimRepository orderClaimRepository;

    public Page<OrderClaimResponse> findByOrderId(UUID orderId, Pageable pageable) {

        return orderClaimRepository.findAllByOrderId(orderId, pageable)
                .map(OrderClaim::toResponse);
    }
}
