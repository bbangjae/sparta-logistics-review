package com.example.sparta.order_service.domain.repository;

import com.example.sparta.order_service.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderQueryRepository {
    Optional<Order> findByCurrentHubId(UUID currentHubId);
    Optional<Order> findByDeliveryId(UUID deliveryId);
    Optional<Order> findByCompanyId(UUID companyId);
}
