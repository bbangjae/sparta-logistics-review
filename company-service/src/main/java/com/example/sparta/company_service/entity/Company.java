package com.example.sparta.company_service.entity;

import com.example.sparta.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_companies")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "company_id")
    @Comment("업체 ID")
    private UUID companyId;

    @Column(nullable = false, length = 100)
    @Comment("업체명")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Comment("업체 유형")
    private CompanyType type;

    @Column(name = "hub_id", nullable = false)
    @Comment("허브 ID")
    private UUID hubId;

    @Column(nullable = false, length = 500)
    @Comment("업체 주소")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Comment("업체 상태")
    @Builder.Default
    private CompanyStatus status = CompanyStatus.ACTIVE;

    public enum CompanyType {
        PRODUCTION, RECEPTION
    }

    public enum CompanyStatus {
        ACTIVE, INACTIVE, DELETED
    }
    
    /**
     * 업체명을 수정합니다.
     * 
     * @param name 새로운 업체명
     */
    public void updateName(String name) {
        this.name = name;
    }
    
    /**
     * 업체 주소를 수정합니다.
     * 
     * @param address 새로운 주소
     */
    public void updateAddress(String address) {
        this.address = address;
    }
    
    /**
     * 업체를 논리적으로 삭제합니다.
     * 
     * 실제 데이터는 유지하며 상태를 INACTIVE로 변경합니다.
     * BaseEntity의 deleted_at, deleted_by 필드가 자동으로 설정됩니다.
     */
    public void softDelete() {
        this.status = CompanyStatus.INACTIVE;
    }
}