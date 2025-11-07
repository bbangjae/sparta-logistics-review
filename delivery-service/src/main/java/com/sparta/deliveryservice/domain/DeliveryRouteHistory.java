package com.sparta.deliveryservice.domain;

import com.sparta.deliveryservice.domain.enums.RouteStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// (테이블명 명시)
@Table(name = "p_delivery_route_histories")
@SQLDelete(sql = "UPDATE delivery_route_history SET deleted_at = NOW() WHERE route_history_id = ?")
@Where(clause = "deleted_at IS NULL")
@Builder // 테스트코드의 builder()를 위해 추가
@AllArgsConstructor // Builder가 모든 필드를 사용하도록 추가
public class DeliveryRouteHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "route_history_id", columnDefinition = "uuid") // 3. name 추가
    private UUID routeHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false) // (이미 snake_case로 되어 있음)
    private Delivery delivery;

    @Column(name = "sequence", nullable = false) // 3. name 추가
    private Integer sequence;

    @Column(name = "origin_hub_id", nullable = false, columnDefinition = "uuid") // 3. name 추가
    private UUID originHubId;

    @Column(name = "destination_hub_id", nullable = false, columnDefinition = "uuid") // 3. name 추가
    private UUID destinationHubId;

    // 예상 및 실제
    @Column(name = "estimated_distance") // 3. name 추가
    private Double estimatedDistance;
    @Column(name = "estimated_duration") // 3. name 추가
    private Integer estimatedDuration;
    @Column(name = "actual_distance") // 3. name 추가
    private Double actualDistance;
    @Column(name = "actual_duration") // 3. name 추가
    private Integer actualDuration;

    @Column(name = "driver_id", columnDefinition = "uuid") // 3. name 추가
    private UUID driverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30) // 3. name 추가
    private RouteStatus status;

    // GREEN 서비스가 호출할 도메인 로직(담당자배정)

    /**
     * 담당자를 배정합니다
     * 이 메서드는 상태(status)를 변경하지 않습니다.
     * @param driverId 배정할 담당자의 UUID
     */
    public void assignDriver(UUID driverId) {
        // 리팩토링 단계에서 여기에 비즈니스 규칙 (이미 배정되었는지를 추가할 수 있다)
        this.driverId = driverId;
    }

    /**
     * GREEN. 경로 배송을 시작합니다
     * TDD 테스트 케이스 2개의 요구사항을 충족시킵니다.
     */
    public void startRoute() {
        // 1. TDD 검증. 담당자 미지정 실패 테스트를 통과시키기 위한 규칙
        if (this.driverId == null) {
            throw new IllegalStateException("담당자가 배정되지 않은 경로는 시작할 수 없습니다.");
        }

        // 2. 다음 TDD를 위한 리팩토링. '대기중' 상태가 아닐 때의 방어 로직
        if (this.status != RouteStatus.WAITING_FOR_TRANSIT) {
            throw new IllegalStateException("이미 시작되었거나 완료된 경로입니다.");
        }

        // 3. TDD 검증. 성공테스트를 통과시키기 위한 상태 변경
        this.status = RouteStatus.IN_TRANSIT;
    }

    /**
     * [GREEN] 경로 배송을 완료(허브 도착)합니다.
     * TDD 테스트 케이스 2개의 요구사항을 충족시킵니다.
     */
    public void completeRoute(Double actualDistance, Integer actualDuration) {

        // 1. [TDD 검증] '상태 불일치 실패' 테스트를 통과시키기 위한 규칙
        if (this.status != RouteStatus.IN_TRANSIT) {
            throw new IllegalStateException("현재 '이동 중(IN_TRANSIT)' 상태인 경로만 완료할 수 있습니다.");
        }

        // 2. [TDD 검증] '성공' 테스트를 통과시키기 위한 상태 및 데이터 변경
        this.status = RouteStatus.ARRIVED_AT_HUB;
        this.actualDistance = actualDistance;
        this.actualDuration = actualDuration;
    }

    void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

}