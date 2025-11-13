package com.sparta.deliveryservice.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryservice.config.RabbitMQConfig;
import com.sparta.deliveryservice.config.RouteMQConfig;
import com.sparta.deliveryservice.domain.OutboxEvent;
import com.sparta.deliveryservice.producer.dto.RouteRequestEvent;
import com.sparta.deliveryservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxWorker {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 5초마다 아웃박스 큐를 체크하여 아직 발송되지 않은 이벤트를 처리
     */
    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();

        for (OutboxEvent event : events) {
            try {
                // JSON -> 실제 이벤트 객체 변환
                Object payload = objectMapper.readValue(event.getPayload(), Object.class);

                // RabbitMQ 발송
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE_NAME,
                        RabbitMQConfig.DELIVERY_CREATED_ROUTING_KEY,
                        payload
                );

                // 발송 성공 시 처리 완료 표시
                event.markProcessed();
                outboxEventRepository.save(event);

                log.info("아웃박스 이벤트 발송 완료: eventId={}", event.getId());
            } catch (Exception e) {
                log.error("아웃박스 이벤트 발송 실패: eventId={}, error={}", event.getId(), e.getMessage());
                // processed=false 그대로 두어 다음 주기에서 재시도
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void processOutbox2() {
        List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();

        for (OutboxEvent event : events) {
            try {
                RouteRequestEvent routeEvent = new ObjectMapper()
                        .readValue(event.getPayload(), RouteRequestEvent.class);

                // 실제 RabbitMQ 발송
                rabbitTemplate.convertAndSend(
                        RouteMQConfig.EXCHANGE_NAME,
                        RouteMQConfig.REQUEST_HUB_ROUTES_ROUTING_KEY,
                        routeEvent,
                        message -> {
                            message.getMessageProperties().setReplyTo(RouteMQConfig.RESPONSE_HUB_ROUTES_QUEUE_NAME);
                            message.getMessageProperties().setCorrelationId(routeEvent.getCorrelationId());
                            return message;
                        }
                );

                event.markProcessed();
                outboxEventRepository.save(event);

                log.info("아웃박스 RouteRequestEvent 발송 완료: correlationId={}", routeEvent.getCorrelationId());
            } catch (Exception e) {
                log.error("RouteRequestEvent 발송 실패: {}", e.getMessage());
                // 실패해도 processed=false 유지 → 다음 주기에서 재시도
            }
        }
    }
}
