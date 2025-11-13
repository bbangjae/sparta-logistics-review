package com.sparta.deliveryservice.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryservice.config.RouteMQConfig;
import com.sparta.deliveryservice.domain.DlqEvent;
import com.sparta.deliveryservice.domain.OutboxEvent;
import com.sparta.deliveryservice.domain.ProcessedEvent;
import com.sparta.deliveryservice.domain.SagaInstance;
import com.sparta.deliveryservice.event.dto.DeliveryCancelEvent;
import com.sparta.deliveryservice.exception.BusinessException;
import com.sparta.deliveryservice.producer.dto.*;
import com.sparta.deliveryservice.repository.DlqEventRepository;
import com.sparta.deliveryservice.repository.OutboxEventRepository;
import com.sparta.deliveryservice.repository.ProcessedEventRepository;
import com.sparta.deliveryservice.repository.SagaInstanceRepository;
import com.sparta.deliveryservice.service.DeliveryAsyncManager;
import com.sparta.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouteEventListener {

    private final OutboxEventRepository outboxEventRepository;
    DeliveryService deliveryService;
    ProcessedEventRepository processedEventRepository;
    SagaInstanceRepository sagaInstanceRepository;
    private final DeliveryAsyncManager deliveryAsyncManager;
    private final DlqEventRepository dlqEventRepository;



    // 메서드에서 트랜잭션이 있으므로 중복으로 인해 장애를 방지하고자 제거
//    @Transactional // 실패시 ACK 되지 않게 처리 -> 예외 발생 시 트랜잭션 롤백 -> 메시지는 다시 큐로 돌아감 (재시도 가능)
    @RabbitListener(queues = RouteMQConfig.RESPONSE_HUB_ROUTES_QUEUE_NAME)
    public void onRouteResponse(RouteResponseEvent event, Message message) throws JsonProcessingException {
        String correlationId = message.getMessageProperties().getCorrelationId();

        // 중복 이벤트 방지
        if (processedEventRepository.existById(correlationId)) {
            log.info("이미 처리된 이벤트: {}", correlationId);
            return;
        }

        // 사가 상태 조회
        SagaInstance saga = sagaInstanceRepository.findByCorrelationId(correlationId)
                .orElseGet(() -> new SagaInstance(correlationId, SagaInstance.SagaStatus.IN_PROGRESS, "RouteResponseReceiver"));

        try {
            // (1) 정상 처리 - Forward 단계
            deliveryService.createDelivery(event, correlationId);

            saga.setStatus(SagaInstance.SagaStatus.COMPLETED);
            saga.setCurrentStep("DeliveryCreated");
            saga.setLastError(null);
            saga.setUpdatedAt(LocalDateTime.now());
            sagaInstanceRepository.save(saga);

            processedEventRepository.save(new ProcessedEvent(correlationId));
            log.info("배송 생성 완료 및 사가 완료: correlationId={}", correlationId);

        } catch (BusinessException e) {
            // (2) 비즈니스 실패 - 보상 수행 (Saga 보상 트랜잭션)
            log.warn("비즈니스 실패 발생, 보상 트랜잭션 수행: correlationId={}, error={}", correlationId, e.getMessage());

            saga.setStatus(SagaInstance.SagaStatus.COMPENSATING);
            saga.setLastError(e.getMessage());
            saga.setUpdatedAt(LocalDateTime.now());
            sagaInstanceRepository.save(saga);

            // Outbox에 보상 이벤트 저장 (보상 트리거)
            DeliveryCancelEvent cancelEvent = new DeliveryCancelEvent(
                    correlationId,
                    deliveryAsyncManager.getPendingRequest(UUID.fromString(correlationId)).getOrderId(),
                    "허브 응답 처리 실패 - 보상 트랜잭션 수행"
            );

            String payload = new ObjectMapper().writeValueAsString(cancelEvent);
            outboxEventRepository.save(new OutboxEvent("DeliveryCancelEvent", payload));

            // ❗ 예외 던지지 않음 → 커밋 (DLQ 이동 X)
            log.info("보상 이벤트 저장 완료. DLQ 이동 생략 (비즈니스 실패)");

        } catch (Exception e) {
            // (3) 시스템 실패 - DLQ 기록 + 재시도 (Spring Retry / RabbitMQ)
            log.error("시스템 오류 발생: correlationId={}, error={}", correlationId, e.getMessage());

            saga.setStatus(SagaInstance.SagaStatus.FAILED);
            saga.setLastError(e.getMessage());
            saga.setUpdatedAt(LocalDateTime.now());
            sagaInstanceRepository.save(saga);

            // DLQ에 즉시 저장 (운영자/모니터링용)
            DlqEvent dlq = new DlqEvent();
            dlq.setEventType("RouteResponseEvent");
            dlq.setPayload(new ObjectMapper().writeValueAsString(event));
            dlq.setErrorMessage(e.getMessage());
            dlq.setRetryCount(0);
            dlqEventRepository.save(dlq);

            // 예외 재던짐 → rollback + DLQ 이동
            throw e;
        }

    }
}
