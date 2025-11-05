package com.example.sparta.company_service.service;

import com.example.sparta.company_service.dto.CompanyResponseDto;
import com.example.sparta.company_service.dto.CompanySearchCriteria;
import com.example.sparta.company_service.entity.Company;
import com.example.sparta.company_service.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 업체 조회 서비스 구현체
 * 
 * CompanyQueryService 인터페이스를 구현하여 업체 조회 비즈니스 로직을 처리합니다.
 * 단일 책임 원칙(SRP)에 따라 조회 로직만 담당하며,
 * 개방-폐쇄 원칙(OCP)에 따라 확장에는 열려있고 수정에는 닫혀있습니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService implements CompanyQueryService {

    private final CompanyRepository companyRepository;

    /**
     * 문자열 파라미터를 받아 검색 조건 객체로 변환하여 조회하는 퍼사드 메서드
     * 
     * 컨트롤러와의 호환성을 위해 유지되며, 내부적으로는 강타입 검색 조건을 사용합니다.
     * 
     * @param name 업체명 검색 키워드
     * @param hubId 허브 ID
     * @param status 상태 문자열
     * @param pageable 페이지네이션 정보
     * @return 검색 결과
     */
    public Page<CompanyResponseDto> getCompanies(String name, UUID hubId, String status, Pageable pageable) {
        log.debug("업체 목록 조회 요청 - name: {}, hubId: {}, status: {}, page: {}", 
                 name, hubId, status, pageable.getPageNumber());
        
        // 검색 조건 객체 생성 (Factory 패턴 적용)
        CompanySearchCriteria searchCriteria = CompanySearchCriteria.builder()
                .name(name)
                .hubId(hubId)
                .status(CompanySearchCriteria.parseStatus(status))
                .build();
        
        return searchCompanies(searchCriteria, pageable);
    }

    /**
     * 검색 조건 객체를 받아 업체 목록을 조회합니다.
     * 
     * 타입 안전성을 보장하며, 검색 조건의 캡슐화를 통해
     * 응집도를 높이고 결합도를 낮춥니다.
     * 
     * @param searchCriteria 검색 조건
     * @param pageable 페이지네이션 정보
     * @return 검색 결과
     */
    @Override
    public Page<CompanyResponseDto> searchCompanies(CompanySearchCriteria searchCriteria, Pageable pageable) {
        log.debug("업체 검색 실행 - 조건: {}", searchCriteria);
        
        // Repository를 통한 데이터 조회
        Page<Company> companies = companyRepository.findCompaniesWithFilters(
                searchCriteria.getName(),
                searchCriteria.getHubId(),
                searchCriteria.getStatus(),
                pageable
        );
        
        log.debug("검색 결과: {}개 업체 조회됨 (전체: {}개)", 
                 companies.getNumberOfElements(), companies.getTotalElements());
        
        // Entity를 DTO로 변환하여 반환
        return companies.map(CompanyResponseDto::from);
    }
}