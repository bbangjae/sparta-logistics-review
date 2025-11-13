package com.sparta.deliveryservice.event;

import com.sparta.deliveryservice.config.RouteMQConfig;
import com.sparta.deliveryservice.domain.DlqQueueLog;
import com.sparta.deliveryservice.repository.DlqQueueLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouteResponseDlqListener {

    private final DlqQueueLogRepository dlqQueueLogRepository;

    @RabbitListener(queues = RouteMQConfig.RESPONSE_HUB_ROUTES_DLQ_NAME)
    public void handleDlqRouteResponse(Message message) {
        try {
            String body = new String(message.getBody());

            DlqQueueLog logEntry = DlqQueueLog.builder()
                    .queueName(RouteMQConfig.RESPONSE_HUB_ROUTES_DLQ_NAME)
                    .payload(body)
                    .build();

            dlqQueueLogRepository.save(logEntry);
            log.warn("DLQ 메시지 수신 및 기록: {}", body);
        } catch (Exception e) {
            log.error("DLQ 리스너 처리 실패: {}", e.getMessage());
            // 예외를 다시 던지면 RabbitMQ 컨테이너가 재시도 / 재전송 처리
            throw e;
        }
    }
}
