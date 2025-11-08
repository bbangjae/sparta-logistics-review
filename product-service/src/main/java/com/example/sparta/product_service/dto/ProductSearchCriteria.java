package com.example.sparta.product_service.dto;

import com.example.sparta.product_service.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 상품 검색 조건 DTO
 * 
 * 클라이언트로부터 받은 검색 조건을 캡슐화하는 데이터 전송 객체입니다.
 * QueryDSL 동적 쿼리 생성에 사용되며, 각 조건은 선택사항입니다.
 * 단일 책임 원칙(SRP)에 따라 검색 조건 관리만 담당합니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCriteria {
    
    /**
     * 상품명 (부분 검색)
     */
    private String name;
    
    /**
     * 업체 ID (완전 일치)
     */
    private UUID companyId;
    
    /**
     * 허브 ID (완전 일치)
     */
    private UUID hubId;
    
    /**
     * 상품 상태 (완전 일치)
     */
    private Product.ProductStatus status;
    
    /**
     * 상품명 검색 조건이 있는지 확인
     * 
     * @return 상품명 검색 조건 존재 여부
     */
    public boolean hasName() {
        return name != null && !name.trim().isEmpty();
    }
    
    /**
     * 업체 ID 검색 조건이 있는지 확인
     * 
     * @return 업체 ID 검색 조건 존재 여부
     */
    public boolean hasCompanyId() {
        return companyId != null;
    }
    
    /**
     * 허브 ID 검색 조건이 있는지 확인
     * 
     * @return 허브 ID 검색 조건 존재 여부
     */
    public boolean hasHubId() {
        return hubId != null;
    }
    
    /**
     * 상품 상태 검색 조건이 있는지 확인
     * 
     * @return 상품 상태 검색 조건 존재 여부
     */
    public boolean hasStatus() {
        return status != null;
    }
    
    /**
     * 정규화된 상품명 반환 (공백 제거)
     * 
     * @return 정규화된 상품명
     */
    public String getNormalizedName() {
        return hasName() ? name.trim() : null;
    }
    
    /**
     * 검색 조건이 하나라도 있는지 확인
     * 
     * @return 검색 조건 존재 여부
     */
    public boolean hasAnySearchCondition() {
        return hasName() || hasCompanyId() || hasHubId() || hasStatus();
    }
}