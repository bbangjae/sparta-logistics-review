package com.sparta.deliveryservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "saga_instance")
public class SagaInstance {

    @Id
    private String correlationId;

    @Enumerated(EnumType.STRING)
    private SagaStatus status;

    private String currentStep;

    private String lastError;

    private LocalDateTime updatedAt = LocalDateTime.now();

    public SagaInstance(String correlationId, SagaStatus sagaStatus, String routeResponseReceiver) {
        this.correlationId = correlationId;
        this.status = SagaStatus.IN_PROGRESS;
        this.currentStep = routeResponseReceiver;
    }

    public SagaInstance() {

    }

    public enum SagaStatus {
        PENDING, // 시작됨 (RouteRequestEvent 발행됨)
        IN_PROGRESS, // 중간 단계 (ex: 허브/배송 경로 응답 대기)
        COMPLETED, // 성공적으로 완료
        COMPENSATING, // 보상 트랜잭션 수행 중
        FAILED // 보상 트랜잭션 실패
    }
}
