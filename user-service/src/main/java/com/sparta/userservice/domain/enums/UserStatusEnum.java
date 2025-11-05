package com.sparta.userservice.domain.enums;

public enum UserStatusEnum {
    PENDING,    // 가입 후 승인 대기
    APPROVED,   // 매니저 승인 완료
    REJECTED,   // 매니저 승인 거절
    DISABLED    // 탈퇴 후 사용 불가
}
