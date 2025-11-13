package com.example.sparta.hub_route_service.infrastructure.messaging.listener;

import com.example.sparta.hub_route_service.application.HubRouteService;
import com.example.sparta.hub_route_service.application.dto.HubRouteResult;
import com.example.sparta.hub_route_service.domain.vo.HubId;
import com.example.sparta.hub_route_service.infrastructure.messaging.config.RabbitMQConfig;
import com.example.sparta.hub_route_service.infrastructure.messaging.event.RouteRequestEvent;
import com.example.sparta.hub_route_service.infrastructure.messaging.event.RouteResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubRouteEventListener {

    private final HubRouteService hubRouteService;
    private final RabbitTemplate rabbitTemplate;

    /**
     * 배송 서비스로부터 허브 경로 요청을 받아 처리하고 응답
     *
     * @param request 허브 경로 요청 (출발 허브 ID, 도착 허브 ID)
     * @param message RabbitMQ 메시지 (correlationId, replyTo 포함)
     */
    @RabbitListener(queues = RabbitMQConfig.HUB_ROUTES_REQUEST_QUEUE)
    public void handleRouteRequest(RouteRequestEvent request, Message message) {
        String correlationId = message
            .getMessageProperties()
            .getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        log.info(
            "허브 경로 요청 수신 - correlationId: {}, 출발 허브: {}, 도착 허브: {}",
            correlationId,
            request.getDepartureHubId(),
            request.getArrivalHubId()
        );

        try {
            // 허브 경로 조회 또는 계산
            HubRouteResult routeResult = hubRouteService.getOrComputeRoute(
                HubId.of(request.getDepartureHubId()),
                HubId.of(request.getArrivalHubId())
            );

            // 응답 이벤트 생성
            RouteResponseEvent response = RouteResponseEvent.from(routeResult);

            // correlationId를 포함하여 응답 전송
            rabbitTemplate.convertAndSend(
                "",
                replyTo,
                response,
                msg -> {
                    msg
                        .getMessageProperties()
                        .setCorrelationId(correlationId);
                    return msg;
                }
            );

            log.info(
                "허브 경로 응답 전송 완료 - correlationId: {}, routeId: {}",
                correlationId,
                routeResult.routeId()
            );
        } catch (Exception e) {
            log.error(
                "허브 경로 요청 처리 실패 - correlationId: {}, 에러: {}",
                correlationId,
                e.getMessage(),
                e
            );
        }
    }
}
