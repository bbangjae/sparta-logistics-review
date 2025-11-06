package com.example.sparta.company_service.service;

import com.example.sparta.company_service.dto.CompanyResponseDto;
import com.example.sparta.company_service.dto.CompanySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 업체 조회 서비스 인터페이스
 * 
 * 업체 조회와 관련된 비즈니스 로직을 정의하며,
 * 구현체의 변경 없이 인터페이스를 통해 의존합니다. (DIP - 의존성 역전 원칙)
 * 단일 책임으로 조회 로직만 담당합니다. (SRP - 단일 책임 원칙)
 */
public interface CompanyQueryService {
    
    /**
     * 검색 조건에 따른 업체 목록 조회
     * 
     * @param searchCriteria 검색 조건
     * @param pageable 페이지네이션 정보
     * @return 조건에 맞는 업체 목록과 페이지네이션 정보
     */
    Page<CompanyResponseDto> searchCompanies(CompanySearchCriteria searchCriteria, Pageable pageable);
}