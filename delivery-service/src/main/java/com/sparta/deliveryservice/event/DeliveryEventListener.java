package com.sparta.deliveryservice.event;

import com.sparta.deliveryservice.config.RabbitMQConfig;
import com.sparta.deliveryservice.producer.dto.DeliveryCompletedEvent;
import com.sparta.deliveryservice.producer.dto.DeliveryCreatedEvent;
import com.sparta.deliveryservice.producer.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeliveryEventListener {

    private final DeliveryEventHandler handler;

    @Transactional // 실패시 ACK 되지 않게 처리 -> 예외 발생 시 트랜잭션 롤백 -> 메시지는 다시 큐로 돌아감 (재시도 가능)
    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE_NAME)
    public void onOrderCreated(OrderCreatedEvent event) {
        handler.handleOrderCreated(event);
    }

    // 배송 생성시 큐
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDeliveryCreated(DeliveryCreatedEvent event) {
        handler.handleDeliveryCreated(event);
    }

    // 배송 완료시 큐
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDeliveryCompleted(DeliveryCompletedEvent event) {
        handler.handleDeliveryCompleted(event);
    }
}
