package com.example.sparta.company_service.service;

import com.example.sparta.company_service.dto.CompanyCreateRequestDto;
import com.example.sparta.company_service.dto.CompanyCreateResponseDto;
import com.example.sparta.company_service.dto.CompanyDeleteResponseDto;
import com.example.sparta.company_service.dto.CompanyResponseDto;
import com.example.sparta.company_service.dto.CompanySearchCriteria;
import com.example.sparta.company_service.dto.CompanyUpdateRequestDto;
import com.example.sparta.company_service.dto.CompanyUpdateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

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
    
    /**
     * 특정 업체 상세 정보 조회
     * 
     * 논리적으로 삭제된 업체는 조회 대상에서 제외되며,
     * 존재하지 않는 업체 ID에 대해서는 404 에러를 반환합니다.
     * 
     * @param companyId 조회할 업체 ID
     * @return 업체 상세 정보
     * @throws com.example.sparta.company_service.exception.CompanyNotFoundException 업체를 찾을 수 없는 경우
     */
    CompanyResponseDto getCompanyById(UUID companyId);
    
    /**
     * 신규 업체 생성
     * 
     * 새로운 업체를 등록하며, 허브 존재 여부와 업체명 중복을 검증합니다.
     * 
     * @param requestDto 업체 생성 요청 정보
     * @return 생성된 업체 정보
     * @throws com.example.sparta.common.exception.BusinessException 허브가 존재하지 않거나 업체명이 중복되는 경우
     */
    CompanyCreateResponseDto createCompany(CompanyCreateRequestDto requestDto);
    
    /**
     * 업체 정보 수정
     * 
     * 특정 업체의 이름, 주소 등을 수정합니다.
     * 부분 업데이트를 지원하며, 업체명 중복을 검증합니다.
     * 
     * @param companyId 수정할 업체 ID
     * @param requestDto 업체 수정 요청 정보
     * @return 수정된 업체 정보
     * @throws com.example.sparta.company_service.exception.CompanyNotFoundException 업체를 찾을 수 없는 경우
     * @throws com.example.sparta.common.exception.BusinessException 업체명이 중복되는 경우
     */
    CompanyUpdateResponseDto updateCompany(UUID companyId, CompanyUpdateRequestDto requestDto);
    
    /**
     * 업체 논리 삭제
     * 
     * 업체를 논리적으로 삭제 처리합니다.
     * 실제 데이터는 유지하며, 상태를 INACTIVE로 변경하고 deleted_at, deleted_by를 기록합니다.
     * 
     * @param companyId 삭제할 업체 ID
     * @return 삭제된 업체 정보
     * @throws com.example.sparta.company_service.exception.CompanyNotFoundException 업체를 찾을 수 없는 경우
     */
    CompanyDeleteResponseDto deleteCompany(UUID companyId);
}