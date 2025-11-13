package com.sparta.deliveryservice.repository;

import com.sparta.deliveryservice.domain.SagaInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SagaInstanceRepository extends JpaRepository<SagaInstance, String> {
    Optional<SagaInstance> findByCorrelationId(String correlationId);
}
