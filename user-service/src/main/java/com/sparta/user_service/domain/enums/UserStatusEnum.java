package com.sparta.user_service.domain.enums;

public enum UserStatusEnum {
    PENDING,    // 가입 후 승인 대기
    APPROVED,   // 매니저 승인 완료
    REJECTED   // 매니저 승인 거절
}
