package com.sparta.deliveryservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class DlqEvent {

    @Id
    @GeneratedValue
    private Long id;

    private String eventType;
    @Lob
    private String payload;
    private String errorMessage;
    private int retryCount = 0;
    private LocalDateTime createdAt = LocalDateTime.now();

    public DlqEvent(String routeResponseEvent, String payload, String message) {
        this.eventType = routeResponseEvent;
        this.payload = payload;
        this.errorMessage = message;
    }

    public DlqEvent() {

    }
}
