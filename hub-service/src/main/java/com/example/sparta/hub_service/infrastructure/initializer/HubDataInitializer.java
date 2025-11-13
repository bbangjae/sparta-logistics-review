package com.example.sparta.hub_service.infrastructure.initializer;

import com.example.sparta.hub_service.domain.entity.Hub;
import com.example.sparta.hub_service.domain.vo.HubAddress;
import com.example.sparta.hub_service.domain.vo.HubCode;
import com.example.sparta.hub_service.domain.vo.Location;
import com.example.sparta.hub_service.infrastructure.repository.HubRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hub 도메인의 테스트 데이터 초기화
 *
 * - local 프로파일에서만 실행
 * - 애플리케이션 시작 시 허브 기본 데이터 생성
 * - HubConnection보다 먼저 실행되어야 함 (Order=1)
 */
@Slf4j
@Profile("local")
@Component
@Order(1)
@RequiredArgsConstructor
public class HubDataInitializer implements ApplicationRunner {

    private final HubRepository hubRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (hubRepository.count() > 0) {
            log.info("Hub data already exists. Skipping initialization.");
            return;
        }

        log.info("Initializing hub test data...");
        List<Hub> hubs = createHubs();
        hubRepository.saveAll(hubs);
        log.info("Created {} hubs successfully", hubs.size());
    }

    /**
     * 허브 기본 데이터 생성
     * 전국 17개 허브 (광역시/도 단위)
     */
    private List<Hub> createHubs() {
        return List.of(
            Hub.create(
                HubCode.SEOUL,
                "서울특별시 센터",
                HubAddress.of("서울특별시 송파구 송파대로 55"),
                Location.of(37.514575, 127.105399)
            ),
            Hub.create(
                HubCode.GYEONGGI_NORTH,
                "경기 북부 센터",
                HubAddress.of("경기도 고양시 덕양구 권율대로 570"),
                Location.of(37.642349, 126.832020)
            ),
            Hub.create(
                HubCode.GYEONGGI_SOUTH,
                "경기 남부 센터",
                HubAddress.of("경기도 이천시 덕평로 257-21"),
                Location.of(37.232060, 127.458671)
            ),
            Hub.create(
                HubCode.BUSAN,
                "부산광역시 센터",
                HubAddress.of("부산 동구 중앙대로 206"),
                Location.of(35.115201, 129.041580)
            ),
            Hub.create(
                HubCode.DAEGU,
                "대구광역시 센터",
                HubAddress.of("대구 북구 태평로 161"),
                Location.of(35.885099, 128.582720)
            ),
            Hub.create(
                HubCode.INCHEON,
                "인천광역시 센터",
                HubAddress.of("인천 남동구 정각로 29"),
                Location.of(37.447344, 126.731604)
            ),
            Hub.create(
                HubCode.GWANGJU,
                "광주광역시 센터",
                HubAddress.of("광주 서구 내방로 111"),
                Location.of(35.152122, 126.889018)
            ),
            Hub.create(
                HubCode.DAEJEON,
                "대전광역시 센터",
                HubAddress.of("대전 서구 둔산로 100"),
                Location.of(36.350469, 127.384826)
            ),
            Hub.create(
                HubCode.ULSAN,
                "울산광역시 센터",
                HubAddress.of("울산 남구 중앙로 201"),
                Location.of(35.541331, 129.335270)
            ),
            Hub.create(
                HubCode.SEJONG,
                "세종특별자치시 센터",
                HubAddress.of("세종특별자치시 한누리대로 2130"),
                Location.of(36.480099, 127.289379)
            ),
            Hub.create(
                HubCode.GANGWON,
                "강원특별자치도 센터",
                HubAddress.of("강원특별자치도 춘천시 중앙로 1"),
                Location.of(37.881315, 127.730197)
            ),
            Hub.create(
                HubCode.CHUNGBUK,
                "충청북도 센터",
                HubAddress.of("충북 청주시 상당구 상당로 82"),
                Location.of(36.636101, 127.488980)
            ),
            Hub.create(
                HubCode.CHUNGNAM,
                "충청남도 센터",
                HubAddress.of("충남 홍성군 홍북읍 충남대로 21"),
                Location.of(36.656449, 126.672900)
            ),
            Hub.create(
                HubCode.JEONBUK,
                "전북특별자치도 센터",
                HubAddress.of("전북특별자치도 전주시 완산구 효자로 225"),
                Location.of(35.814083, 127.147938)
            ),
            Hub.create(
                HubCode.JEONNAM,
                "전라남도 센터",
                HubAddress.of("전남 무안군 삼향읍 오룡길 1"),
                Location.of(34.808585, 126.465100)
            ),
            Hub.create(
                HubCode.GYEONGBUK,
                "경상북도 센터",
                HubAddress.of("경북 안동시 풍천면 도청대로 455"),
                Location.of(36.568339, 128.729501)
            ),
            Hub.create(
                HubCode.GYEONGNAM,
                "경상남도 센터",
                HubAddress.of("경남 창원시 의창구 중앙대로 300"),
                Location.of(35.227824, 128.681400)
            )
        );
    }
}
