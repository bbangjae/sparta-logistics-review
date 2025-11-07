package com.example.sparta.order_service.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "deliveryClient", url = "http://localhost:9005/v1/deliveries")
public interface DeliveryApiClient {
}
