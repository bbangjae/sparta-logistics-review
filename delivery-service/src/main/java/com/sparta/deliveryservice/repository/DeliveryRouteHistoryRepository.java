package com.sparta.deliveryservice.repository;

import com.sparta.deliveryservice.domain.DeliveryRouteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryRouteHistoryRepository extends JpaRepository<DeliveryRouteHistory, UUID> {
}
