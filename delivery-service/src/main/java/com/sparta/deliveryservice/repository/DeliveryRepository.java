package com.sparta.deliveryservice.repository;

import com.sparta.deliveryservice.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID; // 1. UUID 임포트

@Repository
// 2. JpaRepository<Entity, ID타입> -> ID타입을 UUID로 변경
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
}