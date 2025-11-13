package com.sparta.deliveryservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_event")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedEvent {

    @Id
    private String correlationId; // RabbitMQ 메시지 correlationId를 PK로 사용

    private LocalDateTime processedAt;

    public ProcessedEvent(String correlationId) {
        this.correlationId = correlationId;
        this.processedAt = LocalDateTime.now();
    }
}
