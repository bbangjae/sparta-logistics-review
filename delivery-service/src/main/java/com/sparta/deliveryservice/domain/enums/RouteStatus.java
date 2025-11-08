package com.sparta.deliveryservice.domain.enums;

public enum RouteStatus {
    WAITING_FOR_TRANSIT, // 허브 이동 대기중
    IN_TRANSIT, // 허브 이동중
    ARRIVED_AT_HUB // 목적지 허브 도착
}
