package com.example.sparta.order_service.domain.repository;

import com.example.sparta.order_service.presentation.dto.request.SearchCondition;
import com.example.sparta.order_service.presentation.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

public interface OrderQueryRepository {
    Page<OrderResponse> search(SearchCondition condition, String userEmail, Pageable pageable);
}
