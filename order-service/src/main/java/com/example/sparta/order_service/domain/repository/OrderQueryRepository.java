package com.example.sparta.order_service.domain.repository;

import com.example.sparta.order_service.presentation.dto.request.SearchCondition;
import com.example.sparta.order_service.presentation.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryRepository {
    Page<OrderResponse> searchForAdmin(SearchCondition condition, String username, Pageable pageable);
    Page<OrderResponse> searchForHubManager(SearchCondition condition, String username, Pageable pageable);
    Page<OrderResponse> searchForDelivery(SearchCondition condition, String username, Pageable pageable);
    Page<OrderResponse> searchForSupplier(SearchCondition condition, String username, Pageable pageable);
}
