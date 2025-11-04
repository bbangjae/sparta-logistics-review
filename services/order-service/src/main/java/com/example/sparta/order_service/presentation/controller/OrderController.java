package com.example.sparta.order_service.presentation.controller;

import com.example.sparta.order_service.application.service.OrderService;
import com.example.sparta.order_service.presentation.dto.request.OrderRequest;
import com.example.sparta.order_service.presentation.dto.response.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> create(@RequestBody OrderRequest request) {
        return ResponseEntity.created(URI.create("temp"))
                .body(orderService.create(request));
    }
}
