# Sparta Logistics 

## 프로젝트 개요
- **기간**: 2025.10.31 ~ 2025.11.13 (2주)
- **팀 구성**: 5명 (백엔드 개발자)
- **담당 역할**: Hub Service, Hub Route Service 설계 및 구현
- **프로젝트 성격**: MSA 기반 물류 관리 시스템 

---

## 소개
**MSA 기반 물류 관리 시스템** 프로젝트의 전반적인 내용과 제가 담당했던 핵심 역할을 정리하고, 개발 과정에서의 기술적 회고를 작성하였습니다.

기존 Repository 주소: https://github.com/sparta-logitics/sparta_logistics

---

## 담당 영역
- **Hub Service**: 물류 허브 관리 서비스
- **Hub Route Service**: 허브 간 경로 계산 및 관리 서비스

---

## 핵심 구현 기술

### 1. MSA & OpenFeign
- 물류 허브(Hub)와 이동 경로(Hub Route) 도메인을 독립적인 **마이크로서비스로 분리**하여 유연한 확장성 확보
- **Eureka Service Discovery**를 통해 동적으로 서비스 인스턴스를 탐색하고 관리
- **OpenFeign**을 도입하여 서비스 간의 통신을 인터페이스 기반으로 추상화함으로써 결합도를 낮추고 개발 생산성 증대
- Hub Route Service에서 Hub Service의 허브 유효성 검증 API를 **FeignClient**로 호출

### 2. Domain-Driven Design (DDD) & Value Objects
- **도메인 핵심 개념을 Value Object로 캡슐화**: Distance(거리), Duration(소요시간), Location(위경도), HubCode(허브 코드)를 불변 객체로 설계
- **Primitive Obsession 안티패턴 제거**: `Double distance` 대신 `Distance` 타입 사용으로 **안전성 확보**
- **도메인 규칙의 응집**: 유효성 검증(거리 > 0, 위경도 범위 등)을 Value Object 생성자에 집중하여 **비즈니스 로직 분산 방지**
- **Clean Architecture** 적용: Presentation → Application → Domain → Infrastructure 계층 분리
- **의존성 규칙**: 외부 계층 → 내부 계층 방향만 허용, **Domain Layer는 외부 의존성 없음**

### 3. Redis Caching Strategy (Look-aside Pattern)
- 연산 비용이 높은 **'최단 경로 계산 결과'** 와 조회 빈도가 잦은 **'허브 데이터'** 를 Redis에 캐싱
- **Spring Cache Abstraction**(`@Cacheable`, `@CacheEvict`, `@Caching`)을 적용하여 비즈니스 로직과 캐싱 관심사를 분리
- **Look-aside 전략**을 사용하여 Cache Miss 발생 시에만 DB/알고리즘을 수행하고, 데이터 변경 시 즉시 Evict하여 정합성 유지
- 경로 조회 성능을 평균 **10배 이상** 향상 (계산 없이 캐시에서 즉시 반환)

### 4. Dijkstra Algorithm 기반 최적 경로 산출
- Hub Route 서비스의 **핵심 코어 로직**으로, 허브 간 연결망(Graph) 데이터를 기반으로 최단 경로 탐색
- **PriorityQueue(우선순위 큐)** 를 활용한 구현으로 탐색 성능을 최적화 (**Time Complexity: O(E log V)**)
- 단순 직선거리가 아닌, **노드 간 가중치(거리/시간)** 를 반영한 정교한 이동 경로 데이터 생성
- 그래프 구조: `Map<HubId, List<HubConnection>>` 형태의 인접 리스트로 구현
- 경로 역추적(Backtracking)을 통해 출발지부터 도착지까지의 전체 경로 세그먼트 생성

### 5. RabbitMQ 기반 비동기 메시징 & 이벤트 기반 아키텍처
- **배송 서비스(Delivery Service)** 와 **Hub Route Service** 간의 **비동기 통신**으로 구현하여, 기존 HTTP 동기 호출의 Blocking 문제를 해결하고 서비스 간 결합도를 최소화
- 비동기 환경에서도 응답이 필요한 로직 처리를 위해 **Request-Reply 패턴**을 적용했으며, CorrelationId를 통해 요청과 응답 메시지를 정확히 매핑하여 동시다발적 트래픽 상황에서도 데이터 정합성을 보장
- **Direct Exchange + Routing Key** 방식으로 메시지를 정확한 큐로 라우팅
- **Jackson2JsonMessageConverter**를 사용하여 객체를 JSON으로 직렬화/역직렬화
- 특정 서비스의 장애가 전파되지 않도록 시스템을 격리하여 데이터 유실을 방지하고, 메시지 큐를 통한 부하 분산으로 시스템의 **확장성**과 **안정성**을 동시에 확보

### 6. QueryDSL 기반 동적 쿼리 및 검색
- 허브의 **주소, 이름, 상태** 등 다양한 필터링 조건이 조합되는 복합 검색 기능을 구현
- **컴파일 시점**에 쿼리 문법 오류를 사전에 포착하여 런타임 안정성 확보
- **BooleanBuilder**와 Where 절을 활용하여 가독성 높고 확장이 용이한 검색 리포지토리 구축
- Pageable을 통한 **페이징 처리**로 대량 데이터 조회 시에도 성능 유지
 
---

## 기술 스택
| Category | Technology |
|----------|----------|
| Language | Java |
| Framework | Spring Boot, Spring Cloud |
| ORM | Spring Data JPA, QueryDSL |
| Database | PostgreSQL |
| Cache | Redis |
| Message Queue | RabbitMQ |
| Service Discovery | Eureka |
| Config Management | Spring Cloud Config |
| API Communication | OpenFeign |

---

## 아키텍처
```
├── presentation        # Controller Layer
├── application         # Service Layer (비즈니스 로직)
│   ├── command         # Command 객체 (입력)
│   └── dto             # Result 객체 (출력)
├── domain              # Domain Layer
│   ├── entity          # Hub 엔티티
│   ├── service         # Domain Service
│   └── vo              # Value Objects (HubCode, HubStatus, Location 등)
└── infrastructure      # Infrastructure Layer
    ├── repository      # JPA Repository & QueryDSL
    └── initializer     # 데이터 초기화
```

---

## 핵심 구현 내용

### 1. Dijkstra 최단 경로 알고리즘
```java
@Component
public class HubRoutePathFinder {
    public List<HubConnection> findShortestPath(
        HubId start,
        HubId target,
        Map<HubId, List<HubConnection>> graph,
        RouteMetric metric
    ) {
        // 1. 거리 초기화
        Map<HubId, Double> dist = new HashMap<>();
        Map<HubId, HubConnection> prevEdge = new HashMap<>();
        
        // 2. 우선순위 큐로 최소 비용 노드 선택
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(...);
        
        // 3. 다익스트라 탐색
        while (!pq.isEmpty()) {
            // 현재 노드에서 인접 노드로 이동 시 더 짧은 경로 발견 시 갱신
            // ...
        }
        
        // 4. 경로 역추적
        // ...
        return path;
    }
}
```

### 2. 경로 캐싱 전략
```java
@Cacheable(
    cacheNames = "hubRoute",
    key = "#departureHubId.id + '-' + #arrivalHubId.id"
)
public HubRouteResult getOrComputeRoute(
    HubId departureHubId,
    HubId arrivalHubId
) {
    // DB에서 먼저 조회, 없으면 계산
    return hubRouteRepository
        .findDetailedRouteBetween(departureHubId, arrivalHubId)
        .orElseGet(() -> computeAndSaveRoute(departureHubId, arrivalHubId));
}

@CacheEvict(
    cacheNames = "hubRoute",
    key = "#departureHubId.id + '-' + #arrivalHubId.id"
)
public HubRouteResult computeAndSaveRoute(...) {
    // 1. 허브 간 연결 정보 로드
    // 2. 그래프 구성
    // 3. 다익스트라 실행
    // 4. 총 거리/시간 계산
    // 5. 경로 세그먼트 생성 및 저장
}
```

### 3. Value Object로 도메인 개념 표현
```java
@Embeddable
public class Distance {
    private Double distance;
    
    public Distance add(Distance other) {
        return new Distance(this.distance + other.distance);
    }
}

@Embeddable
public class Duration {
    private Integer duration;
    
    public Duration add(Duration other) {
        return new Duration(this.duration + other.duration);
    }
}
```

### 4. FeignClient를 통한 서비스 간 통신
```java
@FeignClient(name = "hub-service")
public interface HubClient {
    @GetMapping("/hubs/{hubId}/exists")
    boolean existsHub(@PathVariable("hubId") UUID hubId);
    
    @GetMapping("/hubs/{hubId}")
    HubDetailResponse getHub(@PathVariable("hubId") UUID hubId);
}
```

---

## 주요 기술 의사결정

### 최단 경로 탐색: Dijkstra 알고리즘 채택
- **대안**: A* 알고리즘, Floyd-Warshall
- **선택 이유**: 
  - 단일 출발지에서 단일 도착지까지의 최단 경로 탐색이 주 목적
  - A*는 휴리스틱 함수가 필요하나, 허브 간 직선거리가 실제 경로와 차이가 큼
  - Floyd-Warshall은 모든 쌍 최단 경로를 계산하므로 오버스펙

### 메시지 브로커: RabbitMQ 채택
- **대안**: Kafka, AWS SQS
- **선택 이유**:
  - Request-Reply 패턴 구현이 용이 (CorrelationId 기본 지원)
  - 학습 곡선이 낮고, Docker로 로컬 환경 구축 간편
  - 경로 조회는 실시간성이 중요하므로 메시지 순서 보장 필요 없음

---

## 어려웠던 점과 해결

### 1. Redis 캐시 정합성 유지의 어려움

**문제 상황**
- 허브 연결(HubConnection)이 변경되면, **관련된 모든 경로(HubRoute) 캐시**를 무효화해야 함
- 예: `서울 → 경기북부` 연결이 변경되면, `서울 → 부산` 경로에도 영향을 미침 (경유지로 사용될 수 있기 때문)
- 초기에는 변경된 연결만 무효화했더니, **이전 캐시 데이터가 반환**되어 잘못된 경로 정보 제공
- 모든 경로 캐시를 무조건 삭제하면 성능 이점이 사라짐

**해결 과정**
1. **캐시 무효화 전략 수립**
   ```java
   // 허브 생성/수정/삭제 → 모든 허브 관련 캐시 삭제
   @Caching(evict = {
       @CacheEvict(cacheNames = "hub", key = "#hubId"),
       @CacheEvict(cacheNames = "hubList", allEntries = true)
   })
   
   // 허브 연결 변경 → 관련 경로 캐시 삭제
   @CacheEvict(cacheNames = "hubRoute", 
               key = "#departureHubId.id + '-' + #arrivalHubId.id")
   ```

2. **Trade-off 결정**
   - **현재 전략**: 허브 연결 변경 시 **모든 경로 캐시를 삭제** (`allEntries = true`)
   - **장점**: 구현이 단순하고 데이터 정합성 보장
   - **단점**: 불필요한 캐시까지 삭제되어 일시적으로 성능 저하
   - **개선 여지**: 영향받는 경로만 선택적으로 삭제 (그래프 분석 필요, 복잡도 증가)

3. **Look-aside 패턴 선택 이유**
   - **Write-through**: 데이터 변경 시 캐시도 함께 업데이트 → 경로 계산이 복잡하여 부적합
   - **Look-aside**: 조회 시 캐시 확인 → 없으면 계산 후 저장 → 읽기 중심 워크로드에 적합

---

### 2. Value Object vs Entity 구분의 모호함

**문제 상황**
- DDD를 학습하며 Value Object의 개념은 알았지만, **어디까지 VO로 만들어야 할지 기준이 모호**했음
- 처음에는 `Double distance`, `Integer duration` 같은 원시 타입을 그대로 사용
- 코드 전반에서 `hub.getLatitude()`, `connection.getDistanceKm()` 같은 필드 접근이 산재
- 비즈니스 규칙(예: 거리는 0보다 커야 함)이 **Service 계층 곳곳에 중복**되어 존재

**해결 과정**
1. **Value Object 도입 기준 정립**
   - ✅ **도메인 의미가 명확한 개념**: Distance(거리), Duration(시간), Location(위치)
   - ✅ **비즈니스 규칙(유효성 검증)이 있는 것**: HubCode(Enum으로 허브 코드 제한)
   - ✅ **식별자가 없고, 값 그 자체로 의미가 있는 것**: `new Distance(100)`과 `new Distance(100)`은 동일
   - ❌ **식별자가 있고, 생명주기를 가진 것**: Hub, HubConnection은 Entity

2. **Value Object 도입 효과**
   - **타입 안전성**: `setDistance(duration)` 같은 실수 방지 (컴파일 타임에 에러)
   - **도메인 규칙 응집**: 거리 관련 검증/계산 로직이 `Distance` 클래스 내부에 집중
   - **코드 가독성**: `distance.add(otherDistance)` 같은 직관적인 표현 가능
   - **불변성**: Setter 없이 생성자로만 생성 → 사이드 이펙트 방지

---

## 프로젝트를 진행하며 배운 내용
1. **도메인 주도 설계(DDD)의 가치:** Entity, VO, Domain Service를 명확히 분리하여 코드의 가독성과 유지보수성을 확보함 
2. **알고리즘의 실용성:** 학습했던 자료구조와 알고리즘 이론이 실제 비즈니스 로직 최적화에 어떻게 적용되는지 경험
3. **분산 환경의 데이터 정합성 난이도:** MSA 환경에서 단순히 캐시를 도입하는 것을 넘어, '캐시 무효화 전략' 등 데이터 정합성을 유지하는 기술적 복잡성을 체감
4. **Trade-off에 기반한 합리적 의사결정:** 캐싱 적용 시 '성능 vs 정합성' 사이의 균형을 고민하며, 기술적 완벽함보다는 비즈니스 요구사항에 부합하는 최적의 선택이 중요함을 학습

--- 

## 추가 개선 부분
- [ ] 시간 기준 최적화 옵션 추가 (현재 거리 기준만 지원)
- [ ] 경로 계산 실패 시 재시도 로직 + Circuit Breaker 패턴 적용
- [ ] Grafana + Prometheus로 경로 조회 성능 모니터링 대시보드 구축
- [ ] 이벤트 기반 아키텍처로 전환 (RabbitMQ → Event Sourcing)
- [ ] 실시간 교통 정보 연동 (외부 API)으로 동적 경로 계산
