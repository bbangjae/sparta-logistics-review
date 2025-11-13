package com.sparta.deliveryservice.producer;

import com.sparta.deliveryservice.client.dto.RouteInfoResponse;
import com.sparta.deliveryservice.config.RouteMQConfig;
import com.sparta.deliveryservice.producer.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RouteRequestMQProducer {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange deliveryExchange;

    /**
     * 허브/경로 서비스에 비동기 요청을 보냄
     */
    public void sendRouteRequestEventAsync(UUID originHubId, UUID destinationHubId, UUID correlationId) {
        RouteRequestEvent request = new RouteRequestEvent(originHubId, destinationHubId, correlationId.toString());

        rabbitTemplate.convertAndSend(
                deliveryExchange.getName(),
                RouteMQConfig.REQUEST_HUB_ROUTES_ROUTING_KEY,
                request,
                message -> {
                    // 메시지 속성에 correlationId 추가
                    message.getMessageProperties().setReplyTo(RouteMQConfig.RESPONSE_HUB_ROUTES_ROUTING_KEY);
                    message.getMessageProperties().setCorrelationId(correlationId.toString());
                    return message;
                }
        );

        // 비동기 발송: convertAndSend는 메시지를 보내고 바로 반환
    }

    /**
     * 허브/경로 서비스에 요청을 보내고, 응답(RouteResponseEvent)을 기다린다.
     */
    public List<RouteInfoResponse> sendRouteRequestEvent(UUID originHubId, UUID destinationHubID, UUID correlationId) {
        RouteRequestEvent request = new RouteRequestEvent(originHubId, destinationHubID, correlationId.toString());

        // 응답을 받을 때까지 기다리는 RPC 패턴
        RouteResponseEvent response = (RouteResponseEvent) rabbitTemplate.convertSendAndReceive(
                deliveryExchange.getName(),
                RouteMQConfig.REQUEST_HUB_ROUTES_ROUTING_KEY,
                request
        );

        if (response == null) {
            throw new IllegalStateException("허브 서비스로부터 응답을 받지 못했습니다.");
        }

        return response.getSegments().stream().map(SegmentResponse::toRouteInfoResponse).toList();
    }
}
