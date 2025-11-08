package com.sparta.deliveryservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EtaPredictRequest {
    // AI가 예측할 수 있도록 경로 정보 목록을 전달
    private List<RouteInfoResponse> routes;

    // 필요시 request.getItems() 같은 상품 정보도 추가
}
