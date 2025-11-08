package com.sparta.deliveryservice.domain.enums;

public enum DeliveryStatus {
    WAITING_AT_HUB, // 허브 대기중
    HUB_TO_HUB, // 허브 이동중
    ARRIVED_AT_DEST_HUB, // 목적지 허브 도착
    COMPANY_DELIVERING, // 업체 이동중
    COMPLETED, // 배송 완료
    FAILED // 배송 실패
}
