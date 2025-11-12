package com.example.sparta.order_service.presentation.controller;

import com.example.sparta.order_service.application.service.OrderQueryService;
import com.example.sparta.order_service.application.service.OrderCommandService;
import com.example.sparta.order_service.presentation.dto.request.OrderRequest;
import com.example.sparta.order_service.presentation.dto.request.OrderUpdateRequest;
import com.example.sparta.order_service.presentation.dto.request.SearchCondition;
import com.example.sparta.order_service.presentation.dto.response.OrderCreateResponse;
import com.example.sparta.order_service.presentation.dto.response.OrderDetailResponse;
import com.example.sparta.order_service.presentation.dto.response.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderCommandService commandService;
    private final OrderQueryService queryService;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> create(@RequestBody @Valid OrderRequest orderRequest,
                                                      @RequestHeader("X-USERNAME") String username,
                                                      @RequestHeader("X-USER-ROLE") String userRole,
                                                      @RequestHeader("X-USERID") UUID userId) {
        return ResponseEntity.created(URI.create("/v1/orders"))
                .body(commandService.create(orderRequest, username, userRole, userId));
    }

    // TODO 헤더에 hubId, deliveryId 등 식별 값 고려
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> search(SearchCondition condition, Pageable pageable, HttpServletRequest request) {
        String username = request.getHeader("X-USERNAME");
        String userRole = request.getHeader("X-USER-ROLE");
        return ResponseEntity.ok(queryService.search(condition, username, userRole, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable UUID id, HttpServletRequest request) {
        String username = request.getHeader("X-USERNAME");
        String userRole = request.getHeader("X-USER-ROLE");
        return ResponseEntity.ok(queryService.findById(id, username, userRole));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> update(@PathVariable UUID id, @RequestBody @Valid OrderUpdateRequest orderUpdateRequest, HttpServletRequest request) {
        String userRole = request.getHeader("X-USER-ROLE");
        return ResponseEntity.ok(commandService.update(id, orderUpdateRequest, userRole));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, HttpServletRequest request) {
        Long userId = Long.getLong(request.getHeader("X-USERID"));
        String userRole = request.getHeader("X-USER-ROLE");
        commandService.delete(id, userId, userRole);
        return ResponseEntity.noContent()
                .location(URI.create("delete-temp-url"))
                .build();
    }

}
