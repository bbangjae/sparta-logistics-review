# Sparta Logistics - MSA 기반 물류 관리 시스템

> Spring Boot 3.5 + Spring Cloud 기반의 대규모 물류 허브 관리 및 배송 최적화 플랫폼

전국 17개 광역 물류 허브를 연결하는 엔터프라이즈급 마이크로서비스 아키텍처 프로젝트입니다.

---

# 목차

- [프로젝트 소개](#프로젝트-소개)
- [개발 환경](#개발-환경)
- [프로젝트 구조](#프로젝트-구조)
- [아키텍처 설계](#아키텍처-설계)
- [프로젝트 실행 가이드](#프로젝트-실행-가이드)
- [설계 산출물](#설계-산출물)
- [API 명세서](#api-명세서)
- [Conventions](#-conventions)
- [트러블슈팅](#트러블슈팅)
- [회고](#회고)
- [팀원소개](#팀원-소개)

# 프로젝트 소개

## 개요

**Sparta Logistics**는 전국 물류 네트워크를 효율적으로 관리하기 위한 MSA 기반 통합 물류 플랫폼입니다. 

17개 광역시/도 단위의 물류 허브를 중심으로 최적 경로 탐색, 실시간 배송 추적, 재고 관리 등 물류 운영의 전 과정을 자동화합니다.

## 주요 기능

### 허브 관리 (Hub Service)
- 전국 17개 광역 물류 허브 CRUD
- 허브 상태 관리 (정상 운영/운영 종료)
- 좌표 기반 위치 정보 관리 (Haversine 거리 계산)
- Redis 캐싱으로 조회 성능 최적화

### 경로 최적화 (Hub Route Service)
- Dijkstra 알고리즘 기반 최단 경로 자동 계산
- 거리/시간 가중치 선택 가능
- 계산된 경로 DB 저장 및 Redis 캐싱
- RabbitMQ를 통한 비동기 경로 조회

### 배송 관리 (Delivery Service)
- 허브 간 배송 생성 및 추적
- 배송 상태 실시간 업데이트
- 경로 정보 자동 연동

### 업체 관리 (Company Service)
- 공급업체/수요업체 등록 및 관리
- 업체별 허브 연결 정보

### 주문 관리 (Order Service)
- 주문 생성 및 배송 요청
- 주문 상태 추적

### 상품 관리 (Product Service)
- 상품 카탈로그 관리
- 재고 관리

### 사용자 인증 (Auth Service)
- JWT 기반 인증/인가
- 역할 기반 접근 제어 (RBAC)

### AI 서비스 (AI Service)
- Gemini AI 연동
- 배송 경로 추천 및 분석

## 기술적 특징

✅ **MSA 아키텍처**: 13개 독립 마이크로서비스로 구성  
✅ **서비스 디스커버리**: Netflix Eureka 기반 동적 서비스 탐색  
✅ **중앙 설정 관리**: Spring Cloud Config Server  
✅ **API Gateway**: 단일 진입점 및 라우팅  
✅ **분산 캐싱**: Redis 활용 다단계 캐싱 전략  
✅ **비동기 메시징**: RabbitMQ 기반 이벤트 드리븐 아키텍처  
✅ **DDD + 헥사고날 아키텍처**: 도메인 중심 설계  
✅ **QueryDSL**: 타입 안전 동적 쿼리  

---

#  개발 환경

| 분류                  | 상세                                                                         |
|---------------------|----------------------------------------------------------------------------|
| **Back-End**        | Java 17, Spring Boot 3.5.7, Spring Cloud 2025.0.0, Spring Data JPA 3.5.5, QueryDSL 5.0.0 (Jakarta), Spring Security 6.5.6 |
| **Database**        | PostgreSQL 18.0, Redis 7.x (Lettuce)                                      |
| **Messaging**       | RabbitMQ 3.x (Spring AMQP)                                                 |
| **Build Tool**      | Gradle 8.10 (Multi-Module)                                                 |
| **Infra**           | Docker Compose, Spring Cloud Config, Netflix Eureka, Spring Cloud Gateway |
| **Open API**        | Google GenAI API, Naver Map API                                            |
| **Testing**         | JUnit5, Mockito, TestContainers                                            |
| **Version Control** | Git, GitHub                                                                |
| **API Docs**        | Swagger UI                                                                 |
| **Communication**   | OpenFeign, RestTemplate                                                    |
| **Monitoring**      | Spring Actuator, Logback                                                   |
| **Tools**           | Lombok, Spring Dotenv                                                      |


---

# 프로젝트 구조

## 전체 디렉토리 구조

```
sparta_logistics/
├── common/                      # 공통 모듈
│   ├── config/                 # 공통 설정 (Redis, QueryDSL, Cache)
│   ├── exception/              # 공통 예외 처리
│   └── util/                   # 공통 유틸리티
│
├── config-server/              # Spring Cloud Config Server
│   └── src/main/resources/
│       └── config-repo/        # Git 기반 설정 저장소
│
├── eureka-server/              # Netflix Eureka Server
│
├── api-gateway/                # Spring Cloud Gateway
│
├── auth-service/               # 인증/인가 서비스
├── user-service/               # 사용자 관리 서비스
├── hub-service/                # 허브 관리 서비스
├── hub-route-service/          # 허브 경로 서비스
├── company-service/            # 업체 관리 서비스
├── product-service/            # 상품 관리 서비스
├── order-service/              # 주문 관리 서비스
├── delivery-service/           # 배송 관리 서비스
├── ai-service/                 # AI 서비스
│
├── build.gradle                # 루트 빌드 설정
├── settings.gradle             # 멀티모듈 설정
├── docker-compose.yml          # 인프라 컨테이너 설정
├── .env                        # 환경 변수
└── README.md                   # 프로젝트 문서
```

---

##  아키텍처 설계

### 네트워크 아키텍처

```
[Client]
   ↓
[API Gateway :8080]
   ↓
[Service Discovery (Eureka) :8761]
   ↓
┌─────────────────────────────────────────┐
│  Microservices                          │
│  - Order Service         :9001          │
│  - Hub Service           :9002          │
│  - Hub Route Service     :9003          │
│  - Company Service       :9004          │
│  - Product Service       :9005          │
│  - Delivery Service      :9006          │
│  - User Service          :9007          │
│  - AI Service            :9008          │
│  - Auth Service          :9009          │
└─────────────────────────────────────────┘
   ↓
┌─────────────────────────────────────────┐
│  Infrastructure                         │
│  - PostgreSQL            :5434          │
│  - Redis                 :6379          │
│  - RabbitMQ              :5672          │
│  - RabbitMQ Management   :15672         │
│  - Config Server         :8888          │
└─────────────────────────────────────────┘
```

### 서비스 포트 매핑

| 서비스 | 포트 | 설명 |
|--------|------|------|
| **Config Server** | 8888 | 중앙 설정 서버 |
| **Eureka Server** | 8761 | 서비스 레지스트리 |
| **API Gateway** | 8080 | API 게이트웨이 |
| **Order Service** | 9001 | 주문 관리 |
| **Hub Service** | 9002 | 허브 관리 |
| **Hub Route Service** | 9003 | 경로 최적화 |
| **Company Service** | 9004 | 업체 관리 |
| **Product Service** | 9005 | 상품 관리 |
| **Delivery Service** | 9006 | 배송 관리 |
| **User Service** | 9007 | 사용자 관리 |
| **AI Service** | 9008 | AI 분석 |
| **Auth Service** | 9009 | 인증/인가 |
| **PostgreSQL** | 5434 | 데이터베이스 |
| **Redis** | 6379 | 캐시 |
| **RabbitMQ** | 5672 | 메시지 브로커 |
| **RabbitMQ UI** | 15672 | 관리 콘솔 |

## MSA 통신 패턴

### 동기 통신 (OpenFeign)
- Hub Service ← Hub Route Service (허브 검증)
- Company Service ← Order Service (업체 정보 조회)
- Product Service ← Order Service (상품 정보 조회)

### 비동기 통신 (RabbitMQ)
- Order Service → Delivery Service (배송 생성 요청)
- Delivery Service → Hub Route Service (경로 조회 요청)
- Hub Route Service → Delivery Service (경로 응답)
- Delivery Service → Order Service (배송 상태 업데이트)

---

# 프로젝트 실행 가이드

### 1. 저장소 클론

```bash
git clone https://github.com/your-repo/sparta_logistics.git
cd sparta_logistics
```

### 2. 환경 변수 설정

프로젝트 루트에 `.env` 파일 생성:

```env
EUREKA_DEFAULT_ZONE=http://eureka-server:8761/eureka/
CONFIG_SERVER_PORT=8888
EUREKA_SERVER_PORT=8761
EUREKA_HOST=eureka-server
API_GATEWAY_PORT=8080
ORDER_SERVICE_PORT=9001
HUB_SERVICE_PORT=9002
HUB_ROUTE_SERVICE_PORT=9003
COMPANY_SERVICE_PORT=9004
PRODUCT_SERVICE_PORT=9005
DELIVERY_SERVICE_PORT=9006
USER_SERVICE_PORT=9007
AI_SERVICE_PORT=9008
AUTH_SERVICE_PORT=9009
RABBIT_HOST=
RABBIT_USERNAME=
RABBIT_PASSWORD=
JWT_SECRET=
JWT_EXPIRATION=
GEMINI_KEY= 
```

### 3. 인프라 서비스 실행


#### Option 1: Docker Compose 사용 (권장)

```bash
# PostgreSQL 시작
docker-compose up -d

# Redis 시작 (별도 docker-compose.yml 필요 시)
docker run -d -p 6379:6379 --name sparta-redis redis:7-alpine

# RabbitMQ 시작
docker run -d -p 5672:5672 -p 15672:15672 --name sparta-rabbitmq rabbitmq:3-management
```

**접속 정보:**
- PostgreSQL: `localhost:5434`
- Redis: `localhost:6379`
- RabbitMQ: `localhost:5672` (관리자: http://localhost:15672, guest/guest)

#### Option 2: 로컬 설치

각 서비스를 직접 설치하여 실행

### 4. 서비스 실행 순서

MSA는 **의존성 순서**에 따라 실행해야 합니다.

#### Step 1: Config Server 실행 (최우선)

```bash
# Windows
gradlew.bat :config-server:bootRun

# Linux/Mac
./gradlew :config-server:bootRun
```

**확인**: http://localhost:8888/actuator/health

#### Step 2: Eureka Server 실행

```bash
./gradlew :eureka-server:bootRun
```

**확인**: http://localhost:8761 (Eureka Dashboard)

#### Step 3: 비즈니스 서비스 실행

30초 대기 후 다음 서비스들을 순서대로 실행:

```bash
# 인증 서비스
./gradlew :auth-service:bootRun

# 사용자 서비스
./gradlew :user-service:bootRun

# 허브 서비스
./gradlew :hub-service:bootRun

# 허브 경로 서비스
./gradlew :hub-route-service:bootRun

# 업체 서비스
./gradlew :company-service:bootRun

# 상품 서비스
./gradlew :product-service:bootRun

# 주문 서비스
./gradlew :order-service:bootRun

# 배송 서비스
./gradlew :delivery-service:bootRun

# AI 서비스
./gradlew :ai-service:bootRun

# API Gateway (마지막)
./gradlew :api-gateway:bootRun
```

#### Step 4: 서비스 등록 확인

Eureka Dashboard(http://localhost:8761)에서 모든 서비스가 등록되었는지 확인

### 5. 빠른 빌드 (테스트 제외)

```bash
# 전체 프로젝트 빌드
./gradlew clean build -x test

# 특정 서비스만 빌드
./gradlew :hub-service:build -x test
./gradlew :hub-route-service:build -x test
```

### 6. JAR 파일로 실행

```bash
# 빌드 후
java -jar config-server/build/libs/config-server-0.0.1-SNAPSHOT.jar
java -jar eureka-server/build/libs/eureka-server-0.0.1-SNAPSHOT.jar
java -jar hub-service/build/libs/hub-service-0.0.1-SNAPSHOT.jar
# ...
```

### 7. 초기 데이터 생성

`local` 프로파일로 실행 시 자동으로 초기 데이터가 생성됩니다:

- **Hub Service**: 17개 허브 자동 생성
- **Hub Route Service**: 272개 허브 간 연결 자동 생성 (17 × 16)


---

# 설계 산출물




## ERD 

<img width="2294" height="1496" alt="erd" src="https://github.com/user-attachments/assets/5adb99a0-26b4-438d-bd34-476461ef8ea7" />


## 시스템 아키텍처 다이어그램

<img width="2408" height="1535" alt="image" src="https://github.com/user-attachments/assets/b6198183-a47c-4ecf-8dc8-7a715e0c78ae" />



## 인프라 설계

<img width="928" height="1232" alt="image" src="https://github.com/user-attachments/assets/bad1789c-1a9b-48d1-ac11-f0832f8933fe" />


## API 명세서

[설계 도메인 개요 및 API 명세서](api-docs.md)

## 네트워크 아키텍처

```
[Client]
   ↓
[API Gateway :8080]
   ↓
[Service Discovery (Eureka) :8761]
   ↓
┌─────────────────────────────────────────┐
│  Microservices                          │
│  - Hub Service                          │
│  - Hub Route Service                    │
│  - Delivery Service                     │
│  - Order Service                        │
│  - Company Service                      │
│  - Product Service                      │
│  - User Service                         │
│  - Auth Service                         │
│  - AI Service                           │
└─────────────────────────────────────────┘
   ↓
┌─────────────────────────────────────────┐
│  Infrastructure                         │
│  - PostgreSQL :5434                     │
│  - Redis :6379                          │
│  - RabbitMQ :5672                       │
│  - Config Server :8888                  │
└─────────────────────────────────────────┘
```


---

# Conventions

[팀 개발 규칙 및 가이드](team-convention.md)


---

# 트러블슈팅

## 1. 최종 일관성(Eventual Consistency) 도입

**어려웠던 점**
- POST /orders API가 배송, 허브 경로 계산까지의 모든 프로세스를 완료하고 최종 결과를 반환해야 한다고 생각하고 높은 결합도로 서비스를 설계했었습니다.

**해결 방안**
- 최종 일관성(Eventual Consistency) 개념을 도입했습니다
- POST /orders는 PENDING 상태의 응답만 즉시 반환하고, 백그라운드에서 RabbitMQ로 실제 처리를 위임
- 클라이언트(App/Web)는 이 orderId로 GET /orders/{id}를 폴링하여 최종 상태(PREPARING 등)를 확인하도록 API 명세를 재정의했습니다.

---

## 2. 발행/응답 시, 비동기 작업의 독립성으로 인한 문제 발생 → 발행/응답의 신뢰성 보장

**어려웠던 점 (초기 문제 발생)**

**문제 개요 (데이터 불일치)**
- 주문 도메인으로부터 배달 생성 요청을 수신하는 과정에서, 배달 생성 트랜잭션은 오류로 실패했으나, 주문 도메인으로의 응답 이벤트("배송 생성됨")는 독립적으로 발행되어 데이터 불일치가 발생했습니다.

**근본 원인**
- 비즈니스 로직(DB 상태 변경)과 외부 통신(이벤트 발행)이 **단일 트랜잭션으로 묶여있지 않아** 한쪽만 성공하고 다른 쪽은 실패하는 원자성(Atomicity) 문제가 발생했습니다.

**해결 방안 (단계별 신뢰성 확보)**

### 1차 해결: 트랜잭션 순서 보장 (After Commit)
- **배경**: 큐로 메시지를 보내는 비동기 작업이 DB 트랜잭션과 독립적으로 동작하는 것을 발견했습니다.
- **조치**: 해당 비동기 이벤트 발행 작업을 **`After Commit`** 로직으로 구현하여, DB 트랜잭션이 성공적으로 완료된 후(커밋 후)에만 이벤트가 작동하도록 순서를 보장했습니다.
- **한계**: After Commit은 **순서만 보장**할 뿐, 이벤트 발행 자체의 **성공(신뢰성)을 보장하지는 않아** 여전히 실패 시 재처리 로직이 필요했습니다.

### 2차 해결: 요청 이벤트 발행 신뢰성 확보 (OUTBOX 패턴)
- **배경**: 1차 해결 후, 허브 도메인으로 경로를 요청하고 응답을 받는 로직에서 동일한 불일치 상황이 재발했습니다.
- **조치**: **OUTBOX 패턴**을 적용했습니다.
  - 배달 상태 변경(DB)과 외부 시스템(허브)으로 보낼 요청 이벤트 기록을 **단일 트랜잭션**으로 묶어 처리했습니다.
  - 별도의 프로세스가 Outbox 테이블을 모니터링하여 이벤트 브로커로 전송, **이벤트 유실을 방지**했습니다.

### 3차 해결: 응답 메시지 소비 신뢰성 확보 (DLQ + 멱등성)
- **배경**: 요청 발행 신뢰성을 확보했으므로, 이제 외부 시스템(허브)으로부터 돌아오는 **응답 메시지 소비**의 신뢰성을 확보하고자 했습니다.
- **조치**:
  - **멱등성(Idempotency)**: 메시지에 포함된 **고유 ID**를 사용하여 컨슈머가 메시지를 중복 처리하지 않도록 구현했습니다.
  - **DLQ (Dead Letter Queue)**: 컨슈머가 메시지 처리에 실패할 경우, 메시지를 DLQ로 보내 **유실을 방지**하고 재시도 또는 수동 처리를 가능하게 했습니다.
- **잔여 문제**: 컨슈머의 응답 처리 로직(DB 상태 변경)은 성공했으나, **이후 후속 이벤트 발행(주문 도메인으로의 응답)이 실패**하는 상황은 여전히 문제로 남았습니다.

### 최종 해결: 후속 이벤트 발행 신뢰성 확보 (컨슈머 Outbox 도입)
- **배경**: 응답 처리 후의 후속 이벤트 발행 실패 문제를 최종적으로 해결해야 했습니다.
- **조치**: **컨슈머 Outbox**를 도입했습니다.
  - 허브 응답 메시지를 받아 **배달 상태를 변경**하는 트랜잭션 내부에서, 주문 도메인으로 보낼 **응답 이벤트를 Outbox 테이블에 함께 기록**하도록 로직을 변경했습니다.
  - 이로써 **배달 상태 변경**과 **응답 이벤트 기록**이 **단일 트랜잭션**으로 원자성을 보장하게 되어, 후속 이벤트 발행의 신뢰성을 완벽하게 확보했습니다.

---

## 3. Circuit Breaker 패턴 도입

**어려웠던 점**
- Product 생성 과정에서 외부 서비스 문제 발생 시 장애 전파 문제 발생

**해결 방안**
- Circuit Breaker 패턴과 Retry 도입
- 외부 서비스 장애 시 격리 조치
- 구체적인 예외 처리를 통해 404, 400 등의 예외는 무시하며 예외별 다른 응답으로 UX 개선

---

## 4. 경로 탐색 알고리즘 선택

**어려웠던 점**
- 17개 허브 간 최적 경로를 계산하기 위해, 전체 경로를 미리 계산할지 혹은 요청 시점에 계산할지를 선택하는 과정이 어려웠음.
- 거리/시간 등 가중치가 요청마다 달라질 수 있어, 전체 재계산 부담을 최소화할 수 있는 성능 전략이 필요했음.

**해결방안**
- **Dijkstra 알고리즘**을 적용하여 요청 시 필요한 경로만 즉시 계산하도록 설계.
- **Redis + DB 캐싱 구조**를 도입해 경로가 변하지 않는 반복 요청의 응답 속도를 크게 향상.
- 가중치를 요청 단위로 동적으로 반영할 수 있어 유연성과 성능을 동시에 확보함.

---

# 회고

## 1. 개발 및 협업 측면에서 잘한 부분

### 1.1 체계적인 아키텍처 설계 및 구현
- MSA 구조와 DDD 기반 설계를 처음 적용하면서도 각 구조의 특징과 장점을 이해하고자 꾸준히 고민했습니다. 이를 설계와 구현에 최대한 반영하기 위해 노력한 결과, MSA 구조에 대한 감각을 기를 수 있었고 프로젝트 설계 능력이 한 단계 성장할 수 있었습니다.

### 1.2 실무 패턴의 성공적인 적용
- Circuit Breaker 패턴을 도입하여 외부 서비스 장애 시 시스템 안정성을 확보했습니다. 특히 모든 예외를 동일하게 처리하던 방식에서 예외별 세분화 설정으로 개선하는 과정을 통해 시스템 안정성의 핵심을 이해할 수 있었습니다.

### 1.3 분산 시스템 데이터 일관성 확보
- After Commit의 한계를 경험한 후 **Outbox 패턴**을 도입하여 DB 트랜잭션과 이벤트 발행을 원자적으로 처리했습니다. 추가로 **Retry와 DLQ**를 구성하여 메시지 유실을 방지하고, 발행(보내기)과 소비(받기) 양쪽 모두에 신뢰성을 확보했습니다. 이를 통해 분산 시스템에서 데이터 일관성을 지키는 것이 얼마나 중요한지 깊이 이해할 수 있었습니다.

### 1.4 효과적인 협업 문화
- 첫 MSA 프로젝트임에도 팀원들 간의 적극적인 코드 리뷰와 피드백을 통해 복잡한 구조를 함께 이해하고 개선해 나갈 수 있었습니다. 특히 서비스가 분리된 환경에서 협업의 중요성을 몸소 체감했으며, 혼자였다면 구조와 흐름을 이해하는 데 훨씬 더 오래 걸렸을 것입니다.

---

## 2. 현재 시스템의 한계와 이를 발전시키기 위한 계획

### 2.1 서비스 간 의존성 관리
**현재 한계**
- 동기 통신(OpenFeign)이 과도하게 사용되어 일부 서비스 장애 시 연쇄 장애 가능성 존재

**개선 계획**
- 비동기 메시징(RabbitMQ) 비중 확대
- 서비스 메시 도입 (Istio 등) 검토
- Saga 패턴 적용으로 분산 트랜잭션 관리 고도화

### 2.2 모니터링 및 관측성(Observability)
**현재 한계**
- 분산 추적(Distributed Tracing) 부재
- 서비스 간 호출 관계 시각화 미흡
- 실시간 알림 체계 부족

**개선 계획**
- Spring Cloud Sleuth + Zipkin 도입으로 분산 추적 구현
- Prometheus + Grafana 기반 메트릭 대시보드 구축
- ELK Stack을 통한 중앙 집중식 로깅 시스템 구축

### 2.3 테스트 자동화
**현재 한계**
- 통합 테스트 커버리지 부족
- E2E 테스트 자동화 미흡

**개선 계획**
- TestContainers 활용한 통합 테스트 강화
- 계약 테스트(Contract Testing) 도입으로 서비스 간 인터페이스 검증
- CI/CD 파이프라인에 자동화된 테스트 단계 추가

### 2.4 성능 최적화
**현재 한계**
- 부하 테스트 미실시
- 병목 구간 식별 부족

**개선 계획**
- JMeter/Gatling을 통한 부하 테스트 수행
- 응답 시간 SLA 정의 및 모니터링
- 데이터베이스 쿼리 최적화 및 인덱싱 전략 수립

---

## 3. 협업 시 아쉽거나 부족했던 부분

### 3.1 초기 설계 단계의 커뮤니케이션
- MSA와 DDD가 모두 처음이다 보니 초기 서비스 경계(Bounded Context)를 정의하는 과정에서 시행착오가 있었습니다. 프로젝트 초반에 더 많은 시간을 들여 도메인 경계와 서비스 책임을 명확히 정의했다면 중간에 구조를 수정하는 시간을 줄일 수 있었을 것입니다.

### 3.2 API 명세 문서화
- 서비스 간 통신이 많은 만큼 API 명세가 중요했지만, 초반에는 문서화가 미흡하여 다른 팀원의 API를 사용할 때 코드를 직접 확인해야 하는 경우가 있었습니다. Swagger를 더 적극적으로 활용하고 계약을 먼저 정의하는 API First 접근 방식을 취했다면 더 효율적이었을 것입니다.

### 3.3 공통 모듈 관리
- 공통 모듈(common)의 변경 사항이 모든 서비스에 영향을 주는 구조였지만, 변경 시 충분한 논의와 공지 없이 진행되어 일부 서비스에서 빌드 오류가 발생한 적이 있었습니다. 공통 모듈 변경 시 사전 협의 프로세스를 확립했다면 좋았을 것입니다.

### 3.4 기술 스터디 및 지식 공유
- MSA 관련 개념(Saga, CQRS, Event Sourcing 등)에 대해 팀원들과 함께 학습하는 시간을 더 가졌다면 설계 단계에서 더 나은 의사결정을 할 수 있었을 것입니다. 정기적인 기술 공유 세션이나 스터디를 진행했다면 팀 전체의 역량이 더 빠르게 향상되었을 것으로 생각됩니다.

### 3.5 코드 리뷰 프로세스
- 코드 리뷰가 이루어지긴 했지만, 일정에 쫓겨 형식적으로 진행된 경우도 있었습니다. 체크리스트를 만들고 리뷰 기준을 명확히 했다면 코드 품질을 더 높일 수 있었을 것입니다.

---

#  팀원 소개

| 팀원  | 깃허브                                          | 담당 서비스                              |
|-----|----------------------------------------------|--------------------------------------|
| 권용은 | [@dnjsals45](https://github.com/dnjsals45)   | User, AI Service 개발 및 테스트코드 작성     |
| 변영재 | [@bbangjae](https://github.com/bbangjae)     | Hub, Hub Route Service 개발 및 테스트코드 작성 |
| 이세준 | [@hello22433](https://github.com/hello22433) | Delivery Service 개발 및 테스트코드 작성     |
| 전우선 | [@wooxexn](https://github.com/wooxexn)       | Product, Company Service 개발 및 테스트코드 작성 |
| 진경천 | [@qqqqq7666](https://github.com/qqqqq7666)   | Order Service 개발 및 테스트코드 작성        |
