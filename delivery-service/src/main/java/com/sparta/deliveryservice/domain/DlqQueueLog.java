package com.sparta.deliveryservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DlqQueueLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String queueName; // 어느 DLQ 큐에서 수신했는지
    @Column(columnDefinition = "TEXT")
    private String payload; // 메시지 내용 JSON
    private LocalDateTime processedAt = LocalDateTime.now(); // 처리 시각
}
