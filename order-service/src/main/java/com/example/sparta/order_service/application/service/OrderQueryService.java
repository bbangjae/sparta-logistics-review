package com.example.sparta.order_service.application.service;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.example.sparta.order_service.domain.entity.Order;
import com.example.sparta.order_service.domain.repository.OrderRepository;
import com.example.sparta.order_service.presentation.dto.request.SearchCondition;
import com.example.sparta.order_service.presentation.dto.response.OrderDetailResponse;
import com.example.sparta.order_service.presentation.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {
    private final OrderRepository orderRepository;

    public Page<OrderResponse> search(SearchCondition condition, String username, String userRole, Pageable pageable) {
        if (pageable.getPageSize() < 0 || pageable.getPageNumber() < 0)
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "페이지 사이즈와 번호는 0 이상이어야합니다.");

        return switch (userRole) {
            case "MASTER" -> orderRepository.searchForAdmin(condition, username, pageable);
            case "HUB_MANAGER" -> orderRepository.searchForHubManager(condition, username, pageable);
            case "DELIVERY_MANAGER" -> orderRepository.searchForDelivery(condition, username, pageable);
            case "SUPPLIER_MANAGER" -> orderRepository.searchForSupplier(condition, username, pageable);
            default -> throw new BusinessException(ErrorCode.INVALID_USER_ROLE);
        };
    }

    public OrderDetailResponse findById(UUID id, String userRole, String username) {
        return switch (userRole) {
            case "MASTER" -> orderRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND))
                    .toDetailResponse();
            case "HUB_MANAGER" -> orderRepository.findByCurrentHubId(id)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND))
                    .toDetailResponse();
            case "DELIVERY_MANAGER" -> orderRepository.findByDeliveryId(id)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND))
                    .toDetailResponse();
            case "SUPPLIER_MANAGER" -> orderRepository.findByCompanyId(id)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND))
                    .toDetailResponse();
            default -> throw new BusinessException(ErrorCode.INVALID_USER_ROLE);
        };
    }
}
