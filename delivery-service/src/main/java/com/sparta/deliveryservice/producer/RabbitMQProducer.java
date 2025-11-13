package com.sparta.deliveryservice.producer;

import com.sparta.deliveryservice.producer.dto.DeliveryCompletedEvent;
import com.sparta.deliveryservice.producer.dto.DeliveryCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQProducer {

    // 2. KafkaTemplate 대신 RabbitTemplate 의존성 주입
    private final RabbitTemplate rabbitTemplate;

    // TODO : 이 값들은 application.yml 또는 @Configuration으로 분리해야 함
    private static final String EXCHANGE_NAME = "delivery.exchange"; // 예시
    private static final String CREATED_ROUTING_KEY = "order.delivery.created"; // 예시
    private static final String COMPLETED_ROUTING_KEY = "order.delivery.completed"; // 예시

    /**
     * 배송 생성 이벤트를 RabbitMQ로 발행합니다.
     * 테스트 작성 필요
     */
    public void sendDeliveryCreatedEvent(DeliveryCreatedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, CREATED_ROUTING_KEY, event);
    }

    /**
     * [GREEN] 배송 완료 이벤트를 RabbitMQ로 발행합니다.
     */
    public void sendDeliveryCompletedEvent(DeliveryCompletedEvent event) {
        log.info("Publishing RabbitMQ Event: orderId={}", event.getOrderId());

        // [TDD 검증] 테스트가 'sendDeliveryCompletedEvent' 호출을 검증(verify)
         rabbitTemplate.convertAndSend(EXCHANGE_NAME, COMPLETED_ROUTING_KEY, event);
    }
}
