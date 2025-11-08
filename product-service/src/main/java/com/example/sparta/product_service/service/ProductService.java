package com.example.sparta.product_service.service;

import com.example.sparta.product_service.dto.ProductResponseDto;
import com.example.sparta.product_service.dto.ProductSearchCriteria;
import com.example.sparta.product_service.entity.Product;
import com.example.sparta.product_service.repository.ProductRepository;
import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 상품 조회 서비스 구현체
 * 
 * 상품 조회와 관련된 비즈니스 로직을 구현합니다.
 * Repository 계층과의 의존성을 인터페이스로 관리하여 결합도를 낮췄습니다. (DIP)
 * 
 * 트랜잭션 관리를 통해 데이터 일관성을 보장하며,
 * 로깅을 통해 운영 시 추적 가능성을 제공합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService implements ProductQueryService {
    
    private final ProductRepository productRepository;
    
    /**
     * 검색 조건에 따른 상품 목록을 조회합니다.
     * 
     * 검색 조건이 없으면 모든 활성 상품을 반환하며,
     * 조건이 있으면 해당 조건에 맞는 상품만 반환합니다.
     * 논리적으로 삭제된 상품은 결과에서 제외됩니다.
     * 
     * @param searchCriteria 검색 조건 (상품명, 업체ID, 허브ID, 상태)
     * @param pageable 페이지네이션 정보
     * @return 조건에 맞는 상품 목록과 페이지 정보
     * @throws BusinessException 조회 중 오류 발생 시
     */
    @Override
    public Page<ProductResponseDto> searchProducts(ProductSearchCriteria searchCriteria, Pageable pageable) {
        log.debug("상품 목록 조회 요청 - 조건: {}, 페이지: {}", searchCriteria, pageable);
        
        try {
            // 검색 조건이 없는 경우 기본 조건 설정 (활성 상품만)
            ProductSearchCriteria effectiveCriteria = searchCriteria;
            if (searchCriteria == null || !searchCriteria.hasAnySearchCondition()) {
                effectiveCriteria = ProductSearchCriteria.builder()
                        .status(Product.ProductStatus.ACTIVE)
                        .build();
                log.debug("검색 조건이 없어 기본 조건 적용 - 활성 상품만 조회");
            }
            
            // Repository를 통한 검색 실행
            Page<Product> productsPage = productRepository.searchProducts(effectiveCriteria, pageable);
            
            // Entity를 DTO로 변환
            Page<ProductResponseDto> responsePage = productsPage.map(ProductResponseDto::from);
            
            log.info("상품 목록 조회 완료 - 총 {}개 상품 중 {}개 조회됨 (페이지 {}/{})",
                    responsePage.getTotalElements(),
                    responsePage.getNumberOfElements(),
                    responsePage.getNumber() + 1,
                    responsePage.getTotalPages());
            
            return responsePage;
            
        } catch (Exception e) {
            log.error("상품 목록 조회 중 오류 발생 - 조건: {}, 오류: {}", searchCriteria, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "상품 목록 조회 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 편의 메서드: 조건 없이 모든 활성 상품 조회
     * 
     * @param pageable 페이지네이션 정보
     * @return 모든 활성 상품 목록
     */
    public Page<ProductResponseDto> getProducts(Pageable pageable) {
        return searchProducts(null, pageable);
    }
    
    /**
     * 특정 상품 상세 정보를 조회합니다.
     * 
     * 존재하지 않거나 논리적으로 삭제된 상품에 대해서는 
     * ProductNotFoundException을 발생시킵니다.
     * 
     * @param productId 조회할 상품 ID
     * @return 상품 상세 정보
     * @throws com.example.sparta.product_service.exception.ProductNotFoundException 상품을 찾을 수 없는 경우
     */
    @Override
    public ProductResponseDto getProductById(UUID productId) {
        log.debug("상품 상세 조회 요청 - productId: {}", productId);
        
        if (productId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "상품 ID는 필수입니다.");
        }
        
        try {
            Product product = productRepository.findById(productId)
                    .filter(p -> p.getDeletedAt() == null) // 논리 삭제된 상품 제외
                    .orElseThrow(() -> {
                        log.warn("조회할 상품을 찾을 수 없음 - productId: {}", productId);
                        return new com.example.sparta.product_service.exception.ProductNotFoundException(productId);
                    });
            
            ProductResponseDto response = ProductResponseDto.from(product);
            
            log.info("상품 상세 조회 완료 - productId: {}, name: {}", 
                    product.getProductId(), product.getName());
            
            return response;
            
        } catch (com.example.sparta.product_service.exception.ProductNotFoundException e) {
            // ProductNotFoundException은 그대로 던짐 (이미 적절한 에러 코드 포함)
            throw e;
        } catch (Exception e) {
            log.error("상품 상세 조회 중 오류 발생 - productId: {}, 오류: {}", productId, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "상품 조회 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 편의 메서드: 쿼리 파라미터를 받아 검색 조건 생성 후 조회
     * 
     * @param name 상품명 (선택사항)
     * @param companyId 업체 ID (선택사항)  
     * @param hubId 허브 ID (선택사항)
     * @param status 상품 상태 (선택사항)
     * @param pageable 페이지네이션 정보
     * @return 조건에 맞는 상품 목록
     */
    public Page<ProductResponseDto> getProducts(String name, String companyId, String hubId, String status, Pageable pageable) {
        log.debug("상품 목록 조회 요청 - name: {}, companyId: {}, hubId: {}, status: {}", 
                 name, companyId, hubId, status);
        
        try {
            ProductSearchCriteria.ProductSearchCriteriaBuilder builder = ProductSearchCriteria.builder();
            
            // 상품명 조건
            if (name != null && !name.trim().isEmpty()) {
                builder.name(name.trim());
            }
            
            // 업체 ID 조건
            if (companyId != null && !companyId.trim().isEmpty()) {
                try {
                    builder.companyId(java.util.UUID.fromString(companyId.trim()));
                } catch (IllegalArgumentException e) {
                    log.warn("잘못된 업체 ID 형식 - companyId: {}", companyId);
                    throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "잘못된 업체 ID 형식입니다.");
                }
            }
            
            // 허브 ID 조건
            if (hubId != null && !hubId.trim().isEmpty()) {
                try {
                    builder.hubId(java.util.UUID.fromString(hubId.trim()));
                } catch (IllegalArgumentException e) {
                    log.warn("잘못된 허브 ID 형식 - hubId: {}", hubId);
                    throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "잘못된 허브 ID 형식입니다.");
                }
            }
            
            // 상품 상태 조건
            if (status != null && !status.trim().isEmpty()) {
                try {
                    builder.status(Product.ProductStatus.valueOf(status.trim().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    log.warn("잘못된 상품 상태 값 - status: {}", status);
                    throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "잘못된 상품 상태 값입니다.");
                }
            }
            
            ProductSearchCriteria searchCriteria = builder.build();
            return searchProducts(searchCriteria, pageable);
            
        } catch (BusinessException e) {
            // BusinessException은 그대로 던짐
            throw e;
        } catch (Exception e) {
            log.error("상품 목록 조회 중 오류 발생 - 오류: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "상품 목록 조회 중 오류가 발생했습니다.", e);
        }
    }
}