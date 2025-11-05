package com.example.sparta.order_service.domain.entity;

public enum OrderStatus {
    PAYMENT_PENDING, PREPARING_FOR_SHIPMENT, SHIPPED,  DELIVERED, COMPLETED, CANCELED, RETURNED
}
