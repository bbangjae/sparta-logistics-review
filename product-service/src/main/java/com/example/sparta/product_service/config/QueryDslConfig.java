package com.example.sparta.product_service.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QueryDSL 설정 클래스
 * 
 * JPAQueryFactory Bean을 생성하여 QueryDSL을 사용할 수 있도록 설정합니다.
 * EntityManager를 주입받아 JPA와 연동됩니다.
 * 
 * 이 설정을 통해 Repository 구현체에서 타입 안전한 동적 쿼리를 작성할 수 있습니다.
 */
@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {

    private final EntityManager entityManager;

    /**
     * JPAQueryFactory Bean 생성
     * 
     * QueryDSL의 핵심 클래스로, 타입 안전한 JPQL 쿼리를 생성할 수 있습니다.
     * Repository 구현체에서 @RequiredArgsConstructor를 통해 주입받아 사용됩니다.
     * 
     * @return JPAQueryFactory 인스턴스
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}