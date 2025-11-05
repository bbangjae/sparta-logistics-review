package com.example.sparta.company_service.controller;

import com.example.sparta.company_service.dto.CompanyResponseDto;
import com.example.sparta.company_service.service.CompanyService;
import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 업체 관리 컨트롤러
 * 
 * 업체와 관련된 HTTP 요청을 처리하며, 마스터 관리자와 허브 관리자가
 * 업체 정보를 조회하고 관리할 수 있는 REST API를 제공합니다.
 */
@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    /**
     * 업체 목록 조회 및 검색 API
     * 
     * 사용자가 업체 조회/검색 화면에서 조건(이름, 허브 등)을 입력하면,
     * 시스템이 권한과 조건을 확인하고 페이지네이션된 결과를 반환합니다.
     * 검색 조건 없이 호출하면 모든 활성 업체를 반환합니다.
     * 
     * @param name 업체명 검색 키워드 (부분 일치, 대소문자 무관)
     * @param hubId 특정 허브에 속한 업체 필터링
     * @param status 업체 상태 필터링 (ACTIVE, INACTIVE, DELETED)
     * @param pageable 페이지네이션 정보 (기본: 10개씩, 생성일 역순)
     * @return 조건에 맞는 업체 목록과 페이지네이션 정보
     */
    @GetMapping
    public ResponseEntity<Page<CompanyResponseDto>> getCompanies(
            @RequestParam(required = false) String name,
            @RequestParam(name = "hub_id", required = false) UUID hubId,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // 페이지 크기 검증
        if (pageable.getPageSize() > 100) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, 
                    "페이지 크기는 100을 초과할 수 없습니다. 요청된 크기: " + pageable.getPageSize());
        }
        
        // 페이지 번호 검증
        if (pageable.getPageNumber() < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, 
                    "페이지 번호는 0 이상이어야 합니다. 요청된 번호: " + pageable.getPageNumber());
        }
        
        Page<CompanyResponseDto> companies = companyService.getCompanies(name, hubId, status, pageable);
        return ResponseEntity.ok(companies);
    }
}