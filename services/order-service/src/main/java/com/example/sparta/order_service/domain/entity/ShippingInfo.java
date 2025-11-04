package com.example.sparta.order_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
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
}