package com.example.sparta.order_service.domain.entity;

import com.example.sparta.order_service.presentation.dto.response.ShippingInfoResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingInfo {
    @Column(nullable = false, length = 100)
    private String companyName;
    @Column(nullable = false, length = 10)
    private String name;
    @Column(nullable = false, length = 11)
    private String phone;
    @Column(nullable = false, length = 50)
    private String address;
    @Column(nullable = false, length = 100)
    private String addressDetail;
    @Column(nullable = false, length = 5)
    private String zipCode;

    @Builder
    public ShippingInfo(String companyName, String name, String phone, String address, String addressDetail, String zipCode) {
        this.companyName = companyName;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.addressDetail = addressDetail;
        this.zipCode = zipCode;
    }

    public ShippingInfoResponse toResponse() {
        return ShippingInfoResponse.builder()
                .address(address)
                .addressDetail(addressDetail)
                .phone(phone)
                .name(name)
                .companyName(companyName)
                .zipCode(zipCode)
                .build();
    }
}