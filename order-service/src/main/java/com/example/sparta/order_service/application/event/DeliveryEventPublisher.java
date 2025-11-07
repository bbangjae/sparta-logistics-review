package com.example.sparta.order_service.application.event;

import com.example.sparta.order_service.domain.client.DeliveryApiClient;
import com.example.sparta.order_service.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryEventPublisher {
    private final DeliveryApiClient deliveryApiClient;
    private final OrderRepository orderRepository;
}
