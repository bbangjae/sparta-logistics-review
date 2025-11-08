package com.example.sparta.product_service.repository;

import com.example.sparta.product_service.dto.ProductSearchCriteria;
import com.example.sparta.product_service.entity.Product;
import com.example.sparta.product_service.entity.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 상품 Repository 커스텀 구현체
 * 
 * QueryDSL을 활용하여 동적 검색 쿼리를 구현합니다.
 * 타입 안전성을 보장하며 복잡한 검색 조건을 효율적으로 처리합니다.
 * 
 * 단일 책임 원칙(SRP)에 따라 검색 관련 데이터 접근 로직만 담당합니다.
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    
    private final JPAQueryFactory queryFactory;
    private static final QProduct product = QProduct.product;
    
    /**
     * 검색 조건에 따른 상품 목록 조회
     * 
     * QueryDSL BooleanBuilder를 사용하여 동적 WHERE 절을 생성하고,
     * 페이지네이션과 정렬을 적용하여 결과를 반환합니다.
     * 논리적으로 삭제된 상품은 항상 제외됩니다.
     * 
     * @param searchCriteria 검색 조건
     * @param pageable 페이지네이션 정보
     * @return 검색 결과와 페이지 정보
     */
    @Override
    public Page<Product> searchProducts(ProductSearchCriteria searchCriteria, Pageable pageable) {
        // 동적 WHERE 절 생성
        BooleanBuilder whereBuilder = createWhereCondition(searchCriteria);
        
        // 정렬 조건 생성
        OrderSpecifier<?>[] orderSpecifiers = createOrderSpecifiers(pageable);
        
        // 데이터 조회 쿼리
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(whereBuilder)
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
        // 전체 개수 조회 쿼리
        Long totalCount = queryFactory
                .select(product.count())
                .from(product)
                .where(whereBuilder)
                .fetchOne();
        
        return new PageImpl<>(products, pageable, totalCount != null ? totalCount : 0);
    }
    
    /**
     * 검색 조건을 바탕으로 동적 WHERE 절을 생성합니다.
     * 
     * @param searchCriteria 검색 조건
     * @return 생성된 BooleanBuilder 조건
     */
    private BooleanBuilder createWhereCondition(ProductSearchCriteria searchCriteria) {
        BooleanBuilder builder = new BooleanBuilder();
        
        // 논리 삭제된 상품 제외 (항상 적용)
        builder.and(product.deletedAt.isNull());
        
        // 상품명 조건 (부분 검색, 대소문자 구분 없음)
        if (searchCriteria.hasName()) {
            builder.and(product.name.containsIgnoreCase(searchCriteria.getNormalizedName()));
        }
        
        // 업체 ID 조건 (완전 일치)
        if (searchCriteria.hasCompanyId()) {
            builder.and(product.companyId.eq(searchCriteria.getCompanyId()));
        }
        
        // 허브 ID 조건 (완전 일치)
        if (searchCriteria.hasHubId()) {
            builder.and(product.hubId.eq(searchCriteria.getHubId()));
        }
        
        // 상품 상태 조건 (완전 일치)
        if (searchCriteria.hasStatus()) {
            builder.and(product.status.eq(searchCriteria.getStatus()));
        }
        
        return builder;
    }
    
    /**
     * Pageable의 정렬 정보를 QueryDSL OrderSpecifier로 변환합니다.
     * 
     * @param pageable 페이지네이션 정보
     * @return 변환된 정렬 조건 배열
     */
    private OrderSpecifier<?>[] createOrderSpecifiers(Pageable pageable) {
        return pageable.getSort().stream()
                .map(this::convertToOrderSpecifier)
                .toArray(OrderSpecifier[]::new);
    }
    
    /**
     * 개별 Sort.Order를 OrderSpecifier로 변환합니다.
     * 
     * @param order 정렬 조건
     * @return 변환된 OrderSpecifier
     */
    private OrderSpecifier<?> convertToOrderSpecifier(Sort.Order order) {
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
        String property = order.getProperty();
        
        return switch (property) {
            case "productId", "product_id" -> new OrderSpecifier<>(direction, product.productId);
            case "name" -> new OrderSpecifier<>(direction, product.name);
            case "companyId", "company_id" -> new OrderSpecifier<>(direction, product.companyId);
            case "hubId", "hub_id" -> new OrderSpecifier<>(direction, product.hubId);
            case "status" -> new OrderSpecifier<>(direction, product.status);
            case "createdAt", "created_at" -> new OrderSpecifier<>(direction, product.createdAt);
            case "updatedAt", "updated_at" -> new OrderSpecifier<>(direction, product.updatedAt);
            default -> new OrderSpecifier<>(direction, product.createdAt); // 기본 정렬: 생성일시
        };
    }
}