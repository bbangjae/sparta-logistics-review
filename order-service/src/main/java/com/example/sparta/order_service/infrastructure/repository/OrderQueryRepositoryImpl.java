package com.example.sparta.order_service.infrastructure.repository;

import com.example.sparta.order_service.domain.entity.Order;
import com.example.sparta.order_service.domain.repository.OrderQueryRepository;
import com.example.sparta.order_service.presentation.dto.response.OrderResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.sparta.order_service.domain.entity.QOrder.order;

@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderResponse> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<OrderResponse> findAllByUserEmail(String userEmail, Pageable pageable) {
        return null;
    }
}
