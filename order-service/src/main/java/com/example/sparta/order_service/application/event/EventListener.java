package com.example.sparta.order_service.application.event;

import com.example.sparta.order_service.application.dto.message.DeliveryCompleteMessage;
import com.example.sparta.order_service.application.dto.message.DeliveryCreatedMessage;
import com.example.sparta.order_service.application.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventListener {
    private final OrderCommandService orderCommandService;

    @RabbitListener(queues = "delivery.queue.order.created")
    public void handleDeliveryCreate(DeliveryCreatedMessage message) {
        try {
            orderCommandService.assignDeliveryId(message);
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("assign delivery process is failed", e);
        }
    }

//  delivery 서비스에서 로직이 구현될 때 까진 주석
//    @RabbitListener(queues = "order.queue.delivery.completed")
//    public void handleDeliveryComplete(DeliveryCompleteMessage message) {
//        try {
//            orderCommandService.deliveryComplete(message);
//        } catch (Exception e) {
//            throw new AmqpRejectAndDontRequeueException("assign delivery process is failed", e);
//        }
//    }
}
