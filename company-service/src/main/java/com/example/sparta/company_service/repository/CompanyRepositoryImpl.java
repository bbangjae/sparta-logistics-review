package com.example.sparta.company_service.repository;

import com.example.sparta.company_service.entity.Company;
import com.example.sparta.company_service.entity.QCompany;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * 업체 Repository 커스텀 구현체
 * 
 * QueryDSL을 사용하여 동적 쿼리를 구현하며,
 * 단일 책임 원칙(SRP)에 따라 데이터 접근 로직만 담당합니다.
 * 각 필터링 조건을 메서드로 분리하여 응집도를 높였습니다.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 다중 조건을 활용한 업체 검색
     * 
     * 검색 조건이 null인 경우 해당 조건은 무시되며,
     * 논리 삭제된 업체는 결과에서 제외됩니다.
     * 
     * @param name 업체명 부분 검색어
     * @param hubId 허브 ID
     * @param status 업체 상태
     * @param pageable 페이지네이션 정보
     * @return 조건에 맞는 업체 목록
     */
    @Override
    public Page<Company> findCompaniesWithFilters(String name, UUID hubId, Company.CompanyStatus status, Pageable pageable) {
        QCompany company = QCompany.company;
        
        log.debug("업체 검색 쿼리 실행 - name: {}, hubId: {}, status: {}", name, hubId, status);

        // 메인 쿼리 작성 - 조건부 where 절 적용
        JPAQuery<Company> query = queryFactory
                .selectFrom(company)
                .where(
                        nameContains(name),      // 업체명 부분 검색
                        hubIdEq(hubId),          // 허브 ID 필터링
                        statusEq(status),        // 상태 필터링
                        company.deletedAt.isNull() // 논리 삭제 제외
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 동적 정렬 적용
        applySorting(query, company, pageable);

        List<Company> content = query.fetch();

        // 성능 최적화된 카운트 쿼리 - 같은 조건으로 별도 실행
        JPAQuery<Long> countQuery = queryFactory
                .select(company.count())
                .from(company)
                .where(
                        nameContains(name),
                        hubIdEq(hubId),
                        statusEq(status),
                        company.deletedAt.isNull()
                );

        log.debug("업체 검색 완료 - 조회된 레코드 수: {}", content.size());
        
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    /**
     * 정렬 조건 동적 적용
     * 
     * 페이지네이션 정보의 정렬 조건을 분석하여 QueryDSL 정렬을 적용합니다.
     * 기본값으로 생성일 역순 정렬을 사용합니다.
     */
    private void applySorting(JPAQuery<Company> query, QCompany company, Pageable pageable) {
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                if ("createdAt".equals(order.getProperty())) {
                    if (order.isAscending()) {
                        query.orderBy(company.createdAt.asc());
                    } else {
                        query.orderBy(company.createdAt.desc());
                    }
                }
                // 추가 정렬 필드는 여기에 확장 가능 (OCP - 개방-폐쇄 원칙)
            });
        } else {
            // 기본 정렬: 생성일 역순
            query.orderBy(company.createdAt.desc());
        }
    }

    /**
     * 업체명 부분 검색 조건 생성
     * 
     * @param name 검색할 업체명 (null 또는 공백시 조건 무시)
     * @return QueryDSL 조건식 또는 null
     */
    private BooleanExpression nameContains(String name) {
        return StringUtils.hasText(name) ? QCompany.company.name.containsIgnoreCase(name) : null;
    }

    /**
     * 허브 ID 일치 조건 생성
     * 
     * @param hubId 검색할 허브 ID (null시 조건 무시)
     * @return QueryDSL 조건식 또는 null
     */
    private BooleanExpression hubIdEq(UUID hubId) {
        return hubId != null ? QCompany.company.hubId.eq(hubId) : null;
    }

    /**
     * 업체 상태 일치 조건 생성
     * 
     * @param status 검색할 상태 (null시 조건 무시)
     * @return QueryDSL 조건식 또는 null
     */
    private BooleanExpression statusEq(Company.CompanyStatus status) {
        return status != null ? QCompany.company.status.eq(status) : null;
    }
}