package com.example.sparta.order_service.presentation.dto.request;

import com.example.sparta.order_service.domain.entity.ShippingInfo;

public record ShippingInfoRequest(String companyName,
                                  String name,
                                  String phone,
                                  String address,
                                  String addressDetail,
                                  String zipCode) {

    public ShippingInfo toEntity() {
        return ShippingInfo.builder()
                .companyName(companyName)
                .name(name)
                .phone(phone)
                .address(address)
                .addressDetail(addressDetail)
                .zipCode(zipCode)
                .build();
    }
}
