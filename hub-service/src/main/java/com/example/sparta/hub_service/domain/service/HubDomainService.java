//package com.example.sparta.hub_service.core.domain;
//
//import com.example.sparta.hub_service.hub.domain.vo.HubCode;
//import org.springframework.stereotype.Component;
//
///**
// * 허브 도메인 서비스
// * - 여러 엔티티를 조율하거나 외부 의존성이 필요한 도메인 로직을 처리
// */
//@Component
//public class HubDomainService {
//
//    /**
//     * 허브 코드 중복 검증 인터페이스
//     * - 도메인 계층에서 정의하지만 구현은 인프라 계층에 위임
//     */
//    public interface HubCodeUniquenessChecker {
//        boolean isDuplicate(HubCode code);
//        boolean isDuplicateExcludingHub(HubCode code, Hub currentHub);
//    }
//
//    private final HubCodeUniquenessChecker uniquenessChecker;
//
//    public HubDomainService(HubCodeUniquenessChecker uniquenessChecker) {
//        this.uniquenessChecker = uniquenessChecker;
//    }
//
//    /**
//     * 새 허브 생성 시 도메인 규칙 검증
//     */
//    public void validateNewHub(HubCode code) {
//        if (uniquenessChecker.isDuplicate(code)) {
//            throw new HubCodeDuplicateException("이미 존재하는 허브 코드입니다: " + code);
//        }
//    }
//
//    /**
//     * 허브 수정 시 도메인 규칙 검증
//     */
//    public void validateHubUpdate(Hub hub, HubCode newCode) {
//        if (!hub.getCode().equals(newCode) &&
//            uniquenessChecker.isDuplicate(newCode)) {
//            throw new HubCodeDuplicateException("이미 존재하는 허브 코드입니다: " + newCode);
//        }
//    }
//}
