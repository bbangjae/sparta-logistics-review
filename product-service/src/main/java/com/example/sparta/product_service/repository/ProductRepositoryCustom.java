package com.example.sparta.product_service.repository;

import com.example.sparta.product_service.dto.ProductSearchCriteria;
import com.example.sparta.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 상품 Repository 커스텀 인터페이스
 * 
 * QueryDSL을 활용한 동적 검색 쿼리를 정의하는 인터페이스입니다.
 * JPA Repository의 기본 기능으로 해결할 수 없는 복잡한 검색 조건을
 * 타입 안전하게 처리하기 위해 분리되었습니다.
 * 
 * 인터페이스 분리 원칙(ISP)에 따라 검색 관련 기능만 정의합니다.
 */
public interface ProductRepositoryCustom {
    
    /**
     * 검색 조건에 따른 상품 목록 조회
     * 
     * QueryDSL을 사용하여 동적 검색 쿼리를 생성하고 실행합니다.
     * 논리적으로 삭제된 상품은 검색 결과에서 제외됩니다.
     * 
     * @param searchCriteria 검색 조건 (상품명, 업체ID, 허브ID, 상태)
     * @param pageable 페이지네이션 정보 (정렬, 페이지 크기, 페이지 번호)
     * @return 검색 조건에 맞는 상품 목록과 페이지 정보
     */
    Page<Product> searchProducts(ProductSearchCriteria searchCriteria, Pageable pageable);
}