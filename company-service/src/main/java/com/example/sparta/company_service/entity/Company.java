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
    
}