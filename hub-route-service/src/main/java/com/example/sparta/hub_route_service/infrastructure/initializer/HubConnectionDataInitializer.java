package com.example.sparta.hub_route_service.infrastructure.initializer;

import com.example.sparta.hub_route_service.domain.entity.HubConnection;
import com.example.sparta.hub_route_service.domain.vo.Distance;
import com.example.sparta.hub_route_service.domain.vo.Duration;
import com.example.sparta.hub_route_service.domain.vo.HubId;
import com.example.sparta.hub_route_service.infrastructure.client.HubClient;
import com.example.sparta.hub_route_service.infrastructure.client.HubDetailResponse;
import com.example.sparta.hub_route_service.infrastructure.repository.HubConnectionRepository;
import feign.FeignException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * HubConnection 테스트 데이터 초기화
 *
 * - local 프로파일에서만 실행
 * - hub-service로부터 허브 목록을 조회하여 모든 허브 간 연결 생성
 * - hub-service가 먼저 실행되어 있어야 함
 *
 * 실행 순서:
 * 1. hub-service 실행 → 허브 데이터 생성
 * 2. hub-route-service 실행 → 이 Initializer가 hub-service 호출하여 연결 생성
 */
@Slf4j
@Profile("local")
@Component
@Order(1)
@RequiredArgsConstructor
public class HubConnectionDataInitializer implements ApplicationRunner {

    private final HubConnectionRepository hubConnectionRepository;
    private final HubClient hubClient;
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (hubConnectionRepository.count() > 0) {
            log.info(
                "HubConnection data already exists. Skipping initialization."
            );
            return;
        }

        log.info("Initializing hub connection test data from hub-service...");

        try {
            // hub-service에서 허브 목록 조회
            List<HubDetailResponse> hubs = hubClient.getAllHubs();

            if (hubs.isEmpty()) {
                log.warn(
                    "No hubs found from hub-service. Skipping connection initialization."
                );
                log.warn("Make sure hub-service is running and has hub data.");
                return;
            }

            log.info("Fetched {} hubs from hub-service", hubs.size());

            // 모든 허브 간 연결 생성
            List<HubConnection> connections = createAllConnections(hubs);
            hubConnectionRepository.saveAll(connections);

            log.info(
                "Successfully created {} hub connections ({} hubs, full-mesh topology)",
                connections.size(),
                hubs.size()
            );
        } catch (FeignException.ServiceUnavailable e) {
            log.error(
                "hub-service is not available. " +
                    "Please start hub-service first before starting hub-route-service."
            );
        } catch (FeignException e) {
            log.error(
                "Failed to fetch hubs from hub-service. " +
                    "Status: {}, Message: {}",
                e.status(),
                e.getMessage()
            );
        } catch (Exception e) {
            log.error("Failed to initialize hub connection data", e);
        }
    }

    /**
     * 모든 허브 간 연결 생성 (P2P 완전 연결 그래프)
     *
     * n개의 허브가 있을 때 n*(n-1)개의 단방향 연결 생성
     * 예: 17개 허브 → 272개 연결 (17 * 16)
     *
     * @param hubs hub-service로부터 조회한 허브 목록
     * @return 생성된 허브 연결 리스트
     */
    private List<HubConnection> createAllConnections(
        List<HubDetailResponse> hubs
    ) {
        List<HubConnection> connections = new ArrayList<>();

        for (HubDetailResponse from : hubs) {
            for (HubDetailResponse to : hubs) {
                if (!from.hubId().equals(to.hubId())) {
                    // Haversine 공식으로 실제 거리 계산
                    double distance = calculateDistance(from, to);
                    int duration = calculateDuration(distance);

                    connections.add(
                        HubConnection.create(
                            HubId.of(from.hubId()),
                            HubId.of(to.hubId()),
                            Distance.of(distance),
                            Duration.of(duration)
                        )
                    );
                }
            }
        }
        return connections;
    }

    /**
     * Haversine 공식을 사용한 두 허브 간 거리 계산
     *
     * @param from 출발 허브
     * @param to 도착 허브
     * @return 거리(km), 실제 도로 거리를 고려한 보정값 적용 (+10~15%)
     */
    private double calculateDistance(
        HubDetailResponse from,
        HubDetailResponse to
    ) {
        final double EARTH_RADIUS_KM = 6371.0;

        double lat1 = from.latitude().doubleValue();
        double lon1 = from.longitude().doubleValue();
        double lat2 = to.latitude().doubleValue();
        double lon2 = to.longitude().doubleValue();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) *
            Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon / 2) *
            Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double straightDistance = EARTH_RADIUS_KM * c;

        // 실제 도로 경로는 직선거리보다 10~15% 더 김 (산, 강, 도로 우회 등)
        double roadDistanceFactor = 1.1 + random.nextDouble() * 0.05;
        double roadDistance = straightDistance * roadDistanceFactor;

        // 소수점 첫째자리까지 반올림
        return Math.round(roadDistance * 10) / 10.0;
    }

    /**
     * 거리 기반 예상 소요 시간 계산
     *
     * 평균 속도: 72km/h (고속도로 기준)
     * 시간(분) = 거리(km) / 속도(km/h) * 60
     *
     * @param distanceKm 거리(km)
     * @return 예상 소요 시간(분)
     */
    private int calculateDuration(double distanceKm) {
        final double AVERAGE_SPEED_KMH = 72.0;
        double durationMinutes = (distanceKm / AVERAGE_SPEED_KMH) * 60;
        return (int) Math.ceil(durationMinutes);
    }
}
