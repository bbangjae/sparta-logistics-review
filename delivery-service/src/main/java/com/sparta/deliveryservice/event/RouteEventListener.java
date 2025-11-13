package com.sparta.deliveryservice.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.deliveryservice.client.dto.RouteInfoResponse;
import com.sparta.deliveryservice.config.RabbitMQConfig;
import com.sparta.deliveryservice.config.RouteMQConfig;
import com.sparta.deliveryservice.domain.ProcessedEvent;
import com.sparta.deliveryservice.domain.dto.request.DeliveryCreateRequest;
import com.sparta.deliveryservice.producer.dto.*;
import com.sparta.deliveryservice.repository.ProcessedEventRepository;
import com.sparta.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouteEventListener {

    DeliveryService deliveryService;
    ProcessedEventRepository processedEventRepository;

    // 메서드에서 트랜잭션이 있으므로 중복으로 인해 장애를 방지하고자 제거
//    @Transactional // 실패시 ACK 되지 않게 처리 -> 예외 발생 시 트랜잭션 롤백 -> 메시지는 다시 큐로 돌아감 (재시도 가능)
    @RabbitListener(queues = RouteMQConfig.RESPONSE_HUB_ROUTES_QUEUE_NAME)
    public void onRouteResponse(RouteResponseEvent event, Message message) throws JsonProcessingException {
        String correlationId = message.getMessageProperties().getCorrelationId();

        if (processedEventRepository.existById(correlationId)) {
            log.info("이미 처리된 이벤트: {}", correlationId);
            return;
        }

        try {
            deliveryService.createDelivery(event, correlationId);
            processedEventRepository.save(new ProcessedEvent(correlationId));
        } catch (Exception e) {
            log.error("처리 실패 - 재시도 예정: {}", e.getMessage());
            throw e; // rollback 자동 재처리
        }
    }
}
