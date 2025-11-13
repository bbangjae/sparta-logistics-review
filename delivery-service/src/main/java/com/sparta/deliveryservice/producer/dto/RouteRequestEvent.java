package com.sparta.deliveryservice.producer.dto;

import com.sparta.deliveryservice.domain.dto.request.DeliveryCreateRequest;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Value
@Getter
@Builder
@AllArgsConstructor
public class RouteRequestEvent {

    // D. 주문 서비스에 전달할 최소한의 정보
    private UUID originHubId;
    private UUID destinationHubId;

    /**
     * 요청-응답 매핑용 correlationId
     * RabbitMQ 메시지 속성에도 동일하게 세팅
     */
    private String correlationId;
}

