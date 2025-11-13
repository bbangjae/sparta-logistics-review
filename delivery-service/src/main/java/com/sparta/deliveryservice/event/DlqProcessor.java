package com.sparta.deliveryservice.event;

import com.sparta.deliveryservice.config.RouteMQConfig;
import com.sparta.deliveryservice.domain.DlqEvent;
import com.sparta.deliveryservice.repository.DlqEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DlqProcessor {

    private final DlqEventRepository dlqEventRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 60000) // 1분 주기
    public void reprocess() {
        List<DlqEvent> items = dlqEventRepository.findAll()
                .stream().filter(e -> e.getRetryCount() < 5).toList();

        for (DlqEvent item : items) {
            try {
                rabbitTemplate.convertAndSend(
                        RouteMQConfig.EXCHANGE_NAME,
                        RouteMQConfig.RESPONSE_HUB_ROUTES_ROUTING_KEY,
                        item.getPayload()
                );

                item.setRetryCount(item.getRetryCount() + 1);
                dlqEventRepository.save(item);
                log.info("DLQ 이벤트 재전송 성공: id={}, retryCount={}", item.getId(), item.getRetryCount());
            } catch (Exception e) {
                item.setRetryCount(item.getRetryCount() + 1);
                item.setErrorMessage(e.getMessage());
                dlqEventRepository.save(item);
                log.warn("DLQ 이벤트 재전송 실패: id={}, error={}", item.getId(), e.getMessage());
            }
        }
    }
}
