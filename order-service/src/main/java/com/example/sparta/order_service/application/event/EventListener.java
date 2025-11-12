package com.example.sparta.order_service.application.event;

import com.example.sparta.order_service.application.dto.message.DeliveryCompleteMessage;
import com.example.sparta.order_service.application.dto.message.DeliveryCreatedMessage;
import com.example.sparta.order_service.application.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener {
    private final OrderCommandService orderCommandService;

    @Value("${mq.order.queue.delivery.created}")
    private String DELIVERY_CREATED_QUEUE;
    @Value("${mq.order.queue.delivery.completed}")
    private String DELIVERY_COMPLETED_QUEUE;

    @RabbitListener(queues = "order.queue.delivery.created")
    public void handleDeliveryCreate(DeliveryCreatedMessage message) {
        try {
            log.info("배송 생성 요청 메시지 발송: {}", message.toString());
            orderCommandService.assignDeliveryId(message);
        } catch (Exception e) {
            log.error("주문 생성 요청 메시지 발송 실패");
            throw new AmqpRejectAndDontRequeueException("assign delivery process is failed", e);
        }
    }


    @RabbitListener(queues = "order.queue.delivery.completed")
    public void handleDeliveryComplete(DeliveryCompleteMessage message) {
        try {
            orderCommandService.deliveryComplete(message);
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("assign delivery process is failed", e);
        }
    }
}
