package com.example.sparta.product_service.service;

import com.example.sparta.product_service.dto.ProductResponseDto;
import com.example.sparta.product_service.dto.ProductSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * 상품 조회 서비스 인터페이스
 * 
 * 상품 조회와 관련된 비즈니스 로직을 정의하며,
 * 구현체의 변경 없이 인터페이스를 통해 의존합니다. (DIP - 의존성 역전 원칙)
 * 단일 책임으로 조회 로직만 담당합니다. (SRP - 단일 책임 원칙)
 */
public interface ProductQueryService {
    
    /**
     * 검색 조건에 따른 상품 목록 조회
     * 
     * 상품명, 업체, 허브, 상태 등의 조건으로 상품을 검색하며,
     * 조건이 없으면 모든 활성 상품을 반환합니다.
     * 논리적으로 삭제된 상품은 조회 대상에서 제외됩니다.
     * 
     * @param searchCriteria 검색 조건
     * @param pageable 페이지네이션 정보
     * @return 조건에 맞는 상품 목록과 페이지네이션 정보
     */
    Page<ProductResponseDto> searchProducts(ProductSearchCriteria searchCriteria, Pageable pageable);
    
    /**
     * 특정 상품 상세 정보 조회
     * 
     * 논리적으로 삭제된 상품은 조회 대상에서 제외되며,
     * 존재하지 않는 상품 ID에 대해서는 404 에러를 반환합니다.
     * 
     * @param productId 조회할 상품 ID
     * @return 상품 상세 정보
     * @throws com.example.sparta.product_service.exception.ProductNotFoundException 상품을 찾을 수 없는 경우
     */
    ProductResponseDto getProductById(UUID productId);
}