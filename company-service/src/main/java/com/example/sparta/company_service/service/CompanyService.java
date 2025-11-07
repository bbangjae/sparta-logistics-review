package com.example.sparta.company_service.service;

import com.example.sparta.company_service.dto.CompanyCreateRequestDto;
import com.example.sparta.company_service.dto.CompanyCreateResponseDto;
import com.example.sparta.company_service.dto.CompanyDeleteResponseDto;
import com.example.sparta.company_service.dto.CompanyResponseDto;
import com.example.sparta.company_service.dto.CompanySearchCriteria;
import com.example.sparta.company_service.dto.CompanyUpdateRequestDto;
import com.example.sparta.company_service.dto.CompanyUpdateResponseDto;
import com.example.sparta.company_service.entity.Company;
import com.example.sparta.company_service.exception.CompanyNotFoundException;
import com.example.sparta.company_service.repository.CompanyRepository;
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
 * 업체 조회 서비스 구현체
 * 
 * CompanyQueryService 인터페이스를 구현하여 업체 조회 비즈니스 로직을 처리합니다.
 * 단일 책임 원칙(SRP)에 따라 조회 로직만 담당하며,
 * 개방-폐쇄 원칙(OCP)에 따라 확장에는 열려있고 수정에는 닫혀있습니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService implements CompanyQueryService {

    private final CompanyRepository companyRepository;

    /**
     * 문자열 파라미터를 받아 검색 조건 객체로 변환하여 조회하는 퍼사드 메서드
     * 
     * 컨트롤러와의 호환성을 위해 유지되며, 내부적으로는 강타입 검색 조건을 사용합니다.
     * 
     * @param name 업체명 검색 키워드
     * @param hubId 허브 ID
     * @param status 상태 문자열
     * @param pageable 페이지네이션 정보
     * @return 검색 결과
     */
    public Page<CompanyResponseDto> getCompanies(String name, UUID hubId, String status, Pageable pageable) {
        log.debug("업체 목록 조회 요청 - name: {}, hubId: {}, status: {}, page: {}", 
                 name, hubId, status, pageable.getPageNumber());
        
        // 검색 조건 객체 생성 (Factory 패턴 적용)
        CompanySearchCriteria searchCriteria = CompanySearchCriteria.builder()
                .name(name)
                .hubId(hubId)
                .status(CompanySearchCriteria.parseStatus(status))
                .build();
        
        return searchCompanies(searchCriteria, pageable);
    }

    /**
     * 검색 조건 객체를 받아 업체 목록을 조회합니다.
     * 
     * 타입 안전성을 보장하며, 검색 조건의 캡슐화를 통해
     * 응집도를 높이고 결합도를 낮춥니다.
     * 
     * @param searchCriteria 검색 조건
     * @param pageable 페이지네이션 정보
     * @return 검색 결과
     */
    @Override
    public Page<CompanyResponseDto> searchCompanies(CompanySearchCriteria searchCriteria, Pageable pageable) {
        log.debug("업체 검색 실행 - 조건: {}", searchCriteria);
        
        try {
            // Repository를 통한 데이터 조회
            Page<Company> companies = companyRepository.findCompaniesWithFilters(
                    searchCriteria.getName(),
                    searchCriteria.getHubId(),
                    searchCriteria.getStatus(),
                    pageable
            );
            
            log.debug("검색 결과: {}개 업체 조회됨 (전체: {}개)", 
                     companies.getNumberOfElements(), companies.getTotalElements());
            
            // Entity를 DTO로 변환하여 반환
            return companies.map(CompanyResponseDto::from);
            
        } catch (Exception e) {
            log.error("업체 검색 중 오류 발생 - 조건: {}, 오류: {}", searchCriteria, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "업체 검색 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 특정 업체의 상세 정보를 조회합니다.
     * 
     * 논리적으로 삭제된 업체는 조회 대상에서 제외되며,
     * 존재하지 않는 업체 ID에 대해서는 CompanyNotFoundException을 발생시킵니다.
     * 
     * @param companyId 조회할 업체 ID
     * @return 업체 상세 정보
     * @throws CompanyNotFoundException 업체를 찾을 수 없는 경우 (404 Not Found)
     */
    @Override
    public CompanyResponseDto getCompanyById(UUID companyId) {
        log.debug("업체 상세 조회 요청 - companyId: {}", companyId);
        
        // 입력값 검증
        if (companyId == null) {
            log.warn("업체 ID가 null로 전달됨");
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "업체 ID는 필수입니다.");
        }
        
        try {
            // Repository에서 업체 조회 (논리 삭제 제외)
            Company company = companyRepository.findById(companyId)
                    .filter(c -> c.getDeletedAt() == null) // 논리 삭제되지 않은 업체만
                    .orElseThrow(() -> {
                        log.warn("업체를 찾을 수 없음 - companyId: {}", companyId);
                        return new CompanyNotFoundException(companyId);
                    });
            
            log.debug("업체 상세 조회 성공 - companyId: {}, name: {}", 
                     company.getCompanyId(), company.getName());
            
            // Entity를 DTO로 변환하여 반환 (단일 책임 원칙 - DTO 변환 책임 분리)
            return CompanyResponseDto.from(company);
            
        } catch (CompanyNotFoundException e) {
            // CompanyNotFoundException은 그대로 던짐 (이미 적절한 에러 코드 포함)
            throw e;
        } catch (Exception e) {
            log.error("업체 상세 조회 중 오류 발생 - companyId: {}, 오류: {}", companyId, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "업체 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 신규 업체를 생성합니다.
     * 
     * 허브 존재 여부를 검증하고 업체명 중복을 확인한 후 새로운 업체를 등록합니다.
     * 비즈니스 규칙에 따라 생성 시점에 ACTIVE 상태로 설정됩니다.
     * 
     * @param requestDto 업체 생성 요청 정보
     * @return 생성된 업체 정보
     * @throws BusinessException 허브가 존재하지 않거나 업체명이 중복되는 경우
     */
    @Override
    @Transactional
    public CompanyCreateResponseDto createCompany(CompanyCreateRequestDto requestDto) {
        log.debug("업체 생성 요청 - name: {}, hubId: {}, type: {}", 
                 requestDto.getName(), requestDto.getHub_id(), requestDto.getType());
        
        // 입력값 검증
        if (requestDto == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "업체 생성 요청 정보는 필수입니다.");
        }
        
        requestDto.validate();
        
        try {
            // 1. 허브 존재 여부 검증 (추후 Hub 서비스 연동 시 구현)
            // TODO: Hub 서비스가 구현되면 실제 허브 존재 여부 검증 추가
            UUID hubId = requestDto.getHub_id();
            if (hubId == null) {
                throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "존재하지 않는 허브 ID입니다.");
            }
            
            // 2. 업체명 중복 검증
            String trimmedName = requestDto.getName().trim();
            boolean nameExists = companyRepository.existsByNameAndDeletedAtIsNull(trimmedName);
            if (nameExists) {
                log.warn("업체명 중복 감지 - name: {}", trimmedName);
                throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, 
                        String.format("이미 존재하는 업체명입니다: %s", trimmedName));
            }
            
            // 3. 업체 Entity 생성 및 저장
            Company company = requestDto.toEntity();
            Company savedCompany = companyRepository.save(company);
            
            log.info("업체 생성 완료 - companyId: {}, name: {}, hubId: {}", 
                    savedCompany.getCompanyId(), savedCompany.getName(), savedCompany.getHubId());
            
            // 4. 응답 DTO 변환 및 반환
            return CompanyCreateResponseDto.from(savedCompany);
            
        } catch (BusinessException e) {
            // BusinessException은 그대로 던짐
            throw e;
        } catch (Exception e) {
            log.error("업체 생성 중 오류 발생 - name: {}, 오류: {}", requestDto.getName(), e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "업체 생성 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 업체 정보를 수정합니다.
     * 
     * 특정 업체의 이름, 주소 등을 부분적으로 수정할 수 있습니다.
     * 업체명이 변경되는 경우 중복 검증을 수행합니다.
     * 
     * @param companyId 수정할 업체 ID
     * @param requestDto 업체 수정 요청 정보
     * @return 수정된 업체 정보
     * @throws CompanyNotFoundException 업체를 찾을 수 없는 경우
     * @throws BusinessException 업체명이 중복되는 경우
     */
    @Override
    @Transactional
    public CompanyUpdateResponseDto updateCompany(UUID companyId, CompanyUpdateRequestDto requestDto) {
        log.debug("업체 수정 요청 - companyId: {}, name: {}, address: {}", 
                 companyId, requestDto.getName(), requestDto.getAddress());
        
        // 입력값 검증
        if (companyId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "업체 ID는 필수입니다.");
        }
        
        if (requestDto == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "업체 수정 요청 정보는 필수입니다.");
        }
        
        requestDto.validate();
        
        // 수정할 필드가 없는 경우
        if (!requestDto.hasFieldsToUpdate()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "수정할 정보를 입력해주세요.");
        }
        
        try {
            // 1. 기존 업체 조회 (논리 삭제된 업체 제외)
            Company existingCompany = companyRepository.findById(companyId)
                    .filter(c -> c.getDeletedAt() == null)
                    .orElseThrow(() -> {
                        log.warn("수정할 업체를 찾을 수 없음 - companyId: {}", companyId);
                        return new CompanyNotFoundException(companyId);
                    });
            
            // 2. 업체명이 변경되는 경우 중복 검증
            if (requestDto.isNameToUpdate()) {
                String newName = requestDto.getNormalizedName();
                String currentName = existingCompany.getName();
                
                // 현재 이름과 다른 경우에만 중복 검증
                if (!newName.equals(currentName)) {
                    boolean nameExists = companyRepository.existsByNameAndDeletedAtIsNull(newName);
                    if (nameExists) {
                        log.warn("업체명 중복 감지 - companyId: {}, newName: {}", companyId, newName);
                        throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, 
                                String.format("이미 존재하는 업체명입니다: %s", newName));
                    }
                }
                
                // 업체명 업데이트
                existingCompany.updateName(newName);
            }
            
            // 3. 주소 업데이트
            if (requestDto.isAddressToUpdate()) {
                String newAddress = requestDto.getNormalizedAddress();
                existingCompany.updateAddress(newAddress);
            }
            
            // 4. 변경사항 저장
            Company updatedCompany = companyRepository.save(existingCompany);
            
            log.info("업체 수정 완료 - companyId: {}, name: {}", 
                    updatedCompany.getCompanyId(), updatedCompany.getName());
            
            // 5. 응답 DTO 변환 및 반환
            return CompanyUpdateResponseDto.from(updatedCompany);
            
        } catch (CompanyNotFoundException e) {
            // CompanyNotFoundException은 그대로 던짐
            throw e;
        } catch (BusinessException e) {
            // BusinessException은 그대로 던짐
            throw e;
        } catch (Exception e) {
            log.error("업체 수정 중 오류 발생 - companyId: {}, 오류: {}", companyId, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "업체 수정 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 업체를 논리적으로 삭제합니다.
     * 
     * 실제 데이터는 유지하며 상태를 INACTIVE로 변경하고,
     * BaseEntity의 deleted_at, deleted_by 필드를 설정합니다.
     * 
     * @param companyId 삭제할 업체 ID
     * @return 삭제된 업체 정보
     * @throws CompanyNotFoundException 업체를 찾을 수 없는 경우
     */
    @Override
    @Transactional
    public CompanyDeleteResponseDto deleteCompany(UUID companyId) {
        log.debug("업체 논리 삭제 요청 - companyId: {}", companyId);
        
        // 입력값 검증
        if (companyId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "업체 ID는 필수입니다.");
        }
        
        try {
            // 1. 기존 업체 조회 (이미 논리 삭제된 업체도 포함)
            Company existingCompany = companyRepository.findById(companyId)
                    .orElseThrow(() -> {
                        log.warn("삭제할 업체를 찾을 수 없음 - companyId: {}", companyId);
                        return new CompanyNotFoundException(companyId);
                    });
            
            // 2. 이미 논리 삭제된 업체인지 확인
            if (existingCompany.getDeletedAt() != null) {
                log.warn("이미 삭제된 업체 - companyId: {}", companyId);
                throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 삭제된 업체입니다.");
            }
            
            // 3. 논리 삭제 처리
            existingCompany.softDelete();
            
            // 4. 변경사항 저장 (BaseEntity의 @PreUpdate가 deleted_at, deleted_by 자동 설정)
            Company deletedCompany = companyRepository.save(existingCompany);
            
            log.info("업체 논리 삭제 완료 - companyId: {}, name: {}", 
                    deletedCompany.getCompanyId(), deletedCompany.getName());
            
            // 5. 응답 DTO 변환 및 반환
            return CompanyDeleteResponseDto.from(deletedCompany);
            
        } catch (CompanyNotFoundException e) {
            // CompanyNotFoundException은 그대로 던짐
            throw e;
        } catch (BusinessException e) {
            // BusinessException은 그대로 던짐
            throw e;
        } catch (Exception e) {
            log.error("업체 논리 삭제 중 오류 발생 - companyId: {}, 오류: {}", companyId, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "업체 삭제 중 오류가 발생했습니다.", e);
        }
    }
}