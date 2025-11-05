package com.example.sparta.common.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        // TODO: Spring Security 인증 구현 후 주석 해제
        /*
         SecurityContextHolder 에서 현재 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

         인증 정보가 없거나 Optional.empty()를 반환
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        principal이 우리가 정의한 UserDetailsImpl 타입이 아닐 경우 Optional.empty() 반환
        if (!(principal instanceof UserDetailsImpl userDetails)) {
            return Optional.empty();
        }
         UserDetailsImpl 에서 사용자 ID를 반환
        return Optional.of(userDetails.getUserId());
        */

        // 임시: 인증 기능 구현 전까지 기본값 반환
        return Optional.of(1L);
    }
}
