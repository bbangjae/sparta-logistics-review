package com.sparta.deliveryservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false) // 1. name 추가
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false) // 1. name 추가
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true) // 1. name 추가
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", nullable = true, columnDefinition = "uuid") // 1. name 추가
    private UUID deletedBy;
}