package com.example.sparta.order_service.application.service;

import com.example.sparta.order_service.domain.repository.OrderQueryRepository;
import com.example.sparta.order_service.presentation.dto.request.SearchCondition;
import com.example.sparta.order_service.presentation.dto.response.OrderResponse;
import com.querydsl.core.types.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderQueryService {
    private final OrderQueryRepository queryRepository;

    public Page<OrderResponse> search(SearchCondition condition, String userEmail) {
        Pageable pageable = PageRequest
                .of(condition.page(), 10, Sort.by(condition.sort()));
        return queryRepository.search(condition, userEmail, pageable);
    }
}
