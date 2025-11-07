package com.example.sparta.order_service.domain.client;

import com.example.sparta.order_service.domain.dto.request.DeliveryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service", url = "http://localhost:9005")
public interface DeliveryApiClient {
    @PostMapping("/v1/deliveries")
    void createDeliveryInfo(@RequestBody DeliveryRequest request);
}
