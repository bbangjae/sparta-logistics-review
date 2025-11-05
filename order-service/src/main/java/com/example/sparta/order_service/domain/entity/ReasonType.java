package com.example.sparta.order_service.domain.entity;

public enum ReasonType {
    // 고객 귀책
    CUSTOMER_REMORSE,
    CUSTOMER_MISTAKE,
    // 판매자/상품 귀책
    // 상품 불량
    PRODUCT_DEFECT,
    // 오배송
    INCORRECT_ITEM_SHIPPED,
    // 배송사 귀책
    // 배송 지연
    DELIVERY_DELAY,
    // 배송 실패
    DELIVERY_FAILED_OR_LOST,


    // --- 기타 ---
    ETC
}
