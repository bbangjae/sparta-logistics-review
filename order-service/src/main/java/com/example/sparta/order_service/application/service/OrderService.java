package com.example.sparta.order_service.application.service;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.example.sparta.order_service.application.exception.OrderStatusException;
import com.example.sparta.order_service.domain.entity.Order;
import com.example.sparta.order_service.domain.repository.OrderRepository;
import com.example.sparta.order_service.presentation.dto.request.OrderRequest;
import com.example.sparta.order_service.presentation.dto.request.OrderUpdateRequest;
import com.example.sparta.order_service.presentation.dto.response.OrderCreateResponse;
import com.example.sparta.order_service.presentation.dto.response.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    // TODO 주문 생성 시 배송 아이디, 허브 아이디 할당
    @Transactional
    public OrderCreateResponse create(OrderRequest request) {
        Order order = request.toEntity();
        order.setUserEmailToCreate("temp");
        return orderRepository.save(order).toCreateResponse();
    }

    @Transactional
    public OrderDetailResponse update(UUID id, OrderUpdateRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND, "id와 일치하는 주문이 존재하지 않습니다. \n id: " + id));

        if(!order.isPreparing())
            throw new OrderStatusException(ErrorCode.ORDER_MODIFICATION_NOT_ALLOWED, "상품이 이미 출고되어 주문 변경이 불가합니다.");

        order.update(request);

        return order.toDetailResponse();
    }

    @Transactional
    public void delete(UUID id, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND, "id와 일치하는 주문이 존재하지 않습니다. \n id: " + id));

        if(!order.isPreparing())
            throw new OrderStatusException(ErrorCode.ORDER_MODIFICATION_NOT_ALLOWED, "상품이 이미 출고되어 주문 변경이 불가합니다.");

        order.delete(userId);
    }
}
