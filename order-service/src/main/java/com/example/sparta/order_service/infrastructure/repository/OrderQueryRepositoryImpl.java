package com.example.sparta.order_service.infrastructure.repository;

import com.example.sparta.order_service.domain.entity.Order;
import com.example.sparta.order_service.domain.repository.OrderQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.sparta.order_service.domain.entity.QOrder.order;

@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<Order>> findAllByUserEmail(String userEmail) {
        return Optional.of(queryFactory.select(order)
                .where(order.userEmail.eq(userEmail))
                .fetch());
    }
}
