package com.sparta.deliveryservice.event;

import com.sparta.deliveryservice.domain.dto.request.DeliveryCreateRequest;
import com.sparta.deliveryservice.producer.RabbitMQProducer;
import com.sparta.deliveryservice.producer.dto.DeliveryCompletedEvent;
import com.sparta.deliveryservice.producer.dto.DeliveryCreatedEvent;
import com.sparta.deliveryservice.producer.dto.OrderCreatedEvent;
import com.sparta.deliveryservice.service.DeliveryAsyncManager;
import com.sparta.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryEventHandler {

    private final RabbitMQProducer rabbitMQProducer;
    private final DeliveryService deliveryService;
    private final DeliveryAsyncManager deliveryAsyncManager;

    public void handleOrderCreated(OrderCreatedEvent event) {
        DeliveryCreateRequest request = DeliveryCreateRequest.from(event);
//        deliveryService.createDeliveryAsync(request);
        deliveryAsyncManager.createDeliveryAsync(request);
    }

    public void handleDeliveryCreated(DeliveryCreatedEvent event) {
        rabbitMQProducer.sendDeliveryCreatedEvent(event);
    }

    public void handleDeliveryCompleted(DeliveryCompletedEvent event) {
        rabbitMQProducer.sendDeliveryCompletedEvent(event);
    }
}
