package com.example.sparta.order_service.presentation.dto.response;

import lombok.Builder;

@Builder
public record ShippingInfoResponse(String companyName,
                                   String name,
                                   String phone,
                                   String address,
                                   String addressDetail,
                                   String zipCode) {
}
