package com.example.sparta.company_service.repository;

import com.example.sparta.company_service.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CompanyRepositoryCustom {
    
    Page<Company> findCompaniesWithFilters(String name, UUID hubId, Company.CompanyStatus status, Pageable pageable);
}