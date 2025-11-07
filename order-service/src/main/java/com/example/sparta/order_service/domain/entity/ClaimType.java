package com.example.sparta.order_service.domain.entity;

public enum ClaimType {
    // 주문 취소
    CANCEL,
    // 환불
    RETURN,
    // 교환
    EXCHANGE,
    // 주문 정보 변경
    CHANGE_INFO,
    // 배송 이슈
    DELIVERY_ISSUE,
    // 관리자 권한 강제 취소
    ADMIN_CANCEL
}
