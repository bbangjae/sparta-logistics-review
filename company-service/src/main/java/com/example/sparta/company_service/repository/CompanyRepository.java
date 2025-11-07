package com.example.sparta.company_service.repository;

import com.example.sparta.company_service.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID>, CompanyRepositoryCustom {
    
    /**
     * 논리적 삭제되지 않은 업체 중 특정 이름을 가진 업체의 존재 여부를 확인합니다.
     * 
     * @param name 검색할 업체명
     * @return 존재 여부 (true: 존재, false: 존재하지 않음)
     */
    boolean existsByNameAndDeletedAtIsNull(String name);
}