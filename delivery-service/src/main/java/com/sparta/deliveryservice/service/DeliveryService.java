package com.sparta.deliveryservice.service;

import com.sparta.deliveryservice.client.AiServiceClient;
import com.sparta.deliveryservice.client.HubRouteServiceClient;
import com.sparta.deliveryservice.client.dto.EtaPredictRequest;
import com.sparta.deliveryservice.client.dto.EtaPredictResponse;
import com.sparta.deliveryservice.client.dto.RouteInfoResponse;
import com.sparta.deliveryservice.domain.Delivery;
import com.sparta.deliveryservice.domain.DeliveryRouteHistory;
import com.sparta.deliveryservice.domain.dto.request.DeliveryCreateRequest;
import com.sparta.deliveryservice.domain.enums.DeliveryStatus;
import com.sparta.deliveryservice.domain.enums.RouteStatus;
import com.sparta.deliveryservice.exception.EntityNotFoundException;
import com.sparta.deliveryservice.repository.DeliveryRepository;
import com.sparta.deliveryservice.repository.DeliveryRouteHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // @Mock이 주입될 생성자
public class DeliveryService {


    // [RED] 테스트가 @Mock으로 주입할 의존성들
    // 의존성 주입은 @RequiredArgsConstructor가 처리
    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteHistoryRepository deliveryRouteHistoryRepository;
    private final HubRouteServiceClient hubRouteServiceClient;
    private final AiServiceClient aiServiceClient;

    /**
     * [TDD] Flow 1: 배송 생성 (Flow 1)
     * TDD 성공 시나리오를 통과하기 위한 실제 구현
     */
    @Transactional
    public void createDelivery(DeliveryCreateRequest request) {

        // 1. B. 허브/경로 서비스 호출
        List<RouteInfoResponse> routes = hubRouteServiceClient.getRoutes(
                request.getOriginHubId(), request.getDestinationHubId()
        );

        // 방어 코드
        if (routes == null || routes.isEmpty()) {
             // 나중에 custom Exception 을 만들어주면 더 좋음
            throw new IllegalArgumentException("유효한 배송 경로를 찾을 수 없습니다.");
        }

        // 2. F. AI 서비스 호출
        EtaPredictRequest aiRequest = new EtaPredictRequest(routes);
        EtaPredictResponse aiResponse = aiServiceClient.calculateEta(aiRequest);

        // 3. 도메인 객체에게 생성을 위임
        Delivery newDelivery = Delivery.createDelivery(request,
                routes,
                aiResponse.getEstimatedArrivalTime());

        // 4. db 저장
         deliveryRepository.save(newDelivery);
    }

    /**
     * [GREEN] Flow 3-1: 최종 담당자 배정
     */
    @Transactional
    public void assignCompanyDriver(UUID deliveryId, UUID companyDriverId) {

        // 1. [TDD 검증] findById 호출 (및 'ID 없음' 예외 처리)
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("배송", deliveryId));

        // 2. [TDD 검증] 도메인 로직 호출
        // (이 메서드가 '경로 미도착' 예외를 던짐)
        delivery.assignCompanyDriver(companyDriverId);

        // 3. [TDD 검증] save 호출 (성공 시에만)
        // JPA Dirver Checking으로 자동 저장 되지만, TDD의 명시적 검증을 위해 save 호출
        deliveryRepository.save(delivery);


    }

    /**
     * [TDD] Flow 3-2: 최종 배송 시작
     * TDD [RED] 테스트 2개를 통과하기 위한 실제 구현
     */
    @Transactional
    public void startCompanyDelivery(UUID deliveryId) {

        // 1. [TDD 검증] findById 호출 (및 'ID 없음' 예외 처리)
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("배송", deliveryId));

        // 2. [TDD 검증] 도메인 로직 호출
        // 이 메서드가 '담당자 미지정' 또는 '상태 이상' 예외를 던짐
        delivery.startCompanyDelivery();

        // 3. [TDD 검증] save 호출 (성공 시에만)
        deliveryRepository.save(delivery);
    }
}


















