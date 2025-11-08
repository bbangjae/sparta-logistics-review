package com.example.sparta.product_service.repository;

import com.example.sparta.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * 상품 Repository
 * 
 * 상품 데이터 접근을 담당하는 Repository 인터페이스입니다.
 * JpaRepository를 상속받아 기본 CRUD 기능을 제공하며,
 * ProductRepositoryCustom을 통해 QueryDSL 기반 동적 쿼리를 지원합니다.
 * 
 * 인터페이스 분리 원칙(ISP)에 따라 필요한 기능만 노출합니다.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {
    
    /**
     * 논리적 삭제되지 않은 상품 중 특정 이름을 가진 상품의 존재 여부를 확인합니다.
     * 
     * @param name 검색할 상품명
     * @return 존재 여부 (true: 존재, false: 존재하지 않음)
     */
    boolean existsByNameAndDeletedAtIsNull(String name);
}