package com.sparta.deliveryservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryservice.domain.OutboxEvent;
import com.sparta.deliveryservice.domain.dto.request.DeliveryCreateRequest;
import com.sparta.deliveryservice.producer.RouteRequestMQProducer;
import com.sparta.deliveryservice.producer.dto.RouteRequestEvent;
import com.sparta.deliveryservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryAsyncManager {

    private final RouteRequestMQProducer routeRequestMQProducer;
    private final OutboxEventRepository outboxEventRepository;

    // 요청-응답 매핑용 Map (ConcurrentHashMap 권장)
    private final Map<UUID, DeliveryCreateRequest> pendingRequests = new ConcurrentHashMap<>();

    public void createDeliveryAsync(DeliveryCreateRequest request) {
        UUID correlationId = UUID.randomUUID(); // 요청-응답 매핑용

        // 1. Pending request 저장 (Optional, 트래킹 용)
        pendingRequests.put(correlationId, request);

        //        routeRequestMQProducer.sendRouteRequestEventAsync(
//                request.getOriginHubId(),
//                request.getDestinationHubId(),
//                correlationId
//        );


        // 아웃박스에 이벤트 저장
        RouteRequestEvent event = new RouteRequestEvent(
                request.getOriginHubId(),
                request.getDestinationHubId(),
                correlationId.toString()
        );


        try {
            String payload = new ObjectMapper().writeValueAsString(event);
            outboxEventRepository.save(new OutboxEvent("RouteRequestEvent", payload));
            log.info("아웃박스에 RouteRequestEvent 저장: correlationId={}", correlationId);
        } catch (Exception e) {
            log.error("아웃박스 저장 실패: {}", e.getMessage());
            throw new RuntimeException(e); // 트랜잭션 롤백
        }
    }

    public DeliveryCreateRequest getPendingRequest(UUID correlationId) {
        return pendingRequests.get(correlationId);
    }

    public void removePendingRequest(UUID correlationId) {
        pendingRequests.remove(correlationId);
    }

}
