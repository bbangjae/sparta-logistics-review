package com.example.sparta.order_service.application.event;

import com.example.sparta.order_service.application.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventListener {
    private final OrderCommandService orderCommandService;

    @RabbitListener(queues = "delivery.order.queue")
    public void handleDeliveryInfo(UUID deliveryId) {

    }
}
