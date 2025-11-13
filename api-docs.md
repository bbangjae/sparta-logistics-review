# Sparta Logistics API 명세서

MSA 기반 물류 관리 시스템 REST API 문서

**Base URL**: `http://localhost:8080` (API Gateway)

---

## 목차

- [인증](#인증)
- [공통 응답 형식](#공통-응답-형식)
- [Hub Service API](#hub-service-api)
- [Hub Route Service API](#hub-route-service-api)
- [Delivery Service API](#delivery-service-api)
- [Order Service API](#order-service-api)
- [Company Service API](#company-service-api)
- [Product Service API](#product-service-api)
- [User Service API](#user-service-api)
- [Auth Service API](#auth-service-api)
- [AI Service API](#ai-service-api)
- [에러 코드](#에러-코드)

---

## 인증

대부분의 API는 JWT 토큰 기반 인증이 필요합니다.

### 요청 헤더
```http
Authorization: Bearer {JWT_TOKEN}
```

### 권한 레벨
- `USER`: 일반 사용자
- `MANAGER`: 관리자
- `MASTER`: 최고 관리자

---

## 공통 응답 형식

### 성공 응답
```json
{
  "data": {
    // 응답 데이터
  },
  "message": "성공 메시지"
}
```

### 에러 응답
```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "에러 메시지",
    "timestamp": "2025-11-13T12:00:00"
  }
}
```

### 페이징 응답
```json
{
  "content": [],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 100,
  "totalPages": 10,
  "last": false,
  "first": true
}
```

---

## Hub Service API

**Base Path**: `/hubs`

**Port**: 9002

### 1. 허브 생성

**Endpoint**
```
POST /hubs
```

**권한**: `MANAGER`, `MASTER`

**Request Body**
```json
{
  "code": "SEOUL",
  "name": "서울특별시 센터",
  "address": "서울특별시 송파구 송파대로 55",
  "latitude": 37.4749,
  "longitude": 127.1240
}
```

**Response** `201 Created`
```json
{
  "hubId": "550e8400-e29b-41d4-a716-446655440000",
  "message": "허브가 성공적으로 생성되었습니다."
}
```

---

### 2. 허브 상세 조회

**Endpoint**
```
GET /hubs/{hubId}
```

**권한**: `USER`, `MANAGER`, `MASTER`

**Response** `200 OK`
```json
{
  "hubId": "550e8400-e29b-41d4-a716-446655440000",
  "code": "SEOUL",
  "name": "서울특별시 센터",
  "address": "서울특별시 송파구 송파대로 55",
  "status": "ACTIVE",
  "location": {
    "latitude": 37.4749,
    "longitude": 127.1240
  },
  "createdAt": "2025-11-13T10:00:00",
  "updatedAt": "2025-11-13T10:00:00"
}
```

**캐싱**: Redis (TTL: 1시간)

---

### 3. 허브 목록 조회

**Endpoint**
```
GET /hubs
```

**권한**: `USER`, `MANAGER`, `MASTER`

**Response** `200 OK`
```json
[
  {
    "hubId": "550e8400-e29b-41d4-a716-446655440000",
    "code": "SEOUL",
    "name": "서울특별시 센터",
    "status": "ACTIVE"
  }
]
```

**캐싱**: Redis (TTL: 10분)

---

### 4. 허브 검색 (페이징)

**Endpoint**
```
GET /hubs/search
```

**권한**: `USER`, `MANAGER`, `MASTER`

**Query Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| name | String | ❌ | 허브 이름 (부분 일치) |
| code | String | ❌ | 허브 코드 |
| status | String | ❌ | 허브 상태 (ACTIVE, CLOSED) |
| address | String | ❌ | 주소 (부분 일치) |
| page | Integer | ❌ | 페이지 번호 (default: 0) |
| size | Integer | ❌ | 페이지 크기 (default: 10) |
| sort | String | ❌ | 정렬 (예: createdAt,desc) |

**Response** `200 OK`
```json
{
  "content": [
    {
      "hubId": "550e8400-e29b-41d4-a716-446655440000",
      "code": "SEOUL",
      "name": "서울특별시 센터",
      "status": "ACTIVE"
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 5. 허브 수정

**Endpoint**
```
PUT /hubs/{hubId}
```

**권한**: `MANAGER`, `MASTER`

**Request Body**
```json
{
  "code": "SEOUL",
  "name": "서울특별시 메인 센터",
  "address": "서울특별시 송파구 송파대로 55",
  "status": "ACTIVE",
  "latitude": 37.4749,
  "longitude": 127.1240
}
```

**Response** `200 OK`

---

### 6. 허브 삭제

**Endpoint**
```
DELETE /hubs/{hubId}
```

**권한**: `MASTER`

**Response** `204 No Content`

---

### 7. 허브 존재 확인

**Endpoint**
```
GET /hubs/{hubId}/exists
```

**권한**: 내부 서비스

**Response** `200 OK`
```json
true
```

---

## Hub Route Service API

**Base Path**: `/hub-routes`, `/hub-connections`

**Port**: 9003

### 1. 최적 경로 조회

**Endpoint**
```
GET /hub-routes
```

**권한**: `USER`, `MANAGER`, `MASTER`

**Query Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| departureHubId | UUID | ✅ | 출발 허브 ID |
| arrivalHubId | UUID | ✅ | 도착 허브 ID |

**Response** `200 OK`
```json
{
  "routeId": "770e8400-e29b-41d4-a716-446655440002",
  "departureHubId": "550e8400-e29b-41d4-a716-446655440000",
  "arrivalHubId": "660e8400-e29b-41d4-a716-446655440001",
  "segments": [
    {
      "sequence": 1,
      "departureHubId": "550e8400-e29b-41d4-a716-446655440000",
      "arrivalHubId": "880e8400-e29b-41d4-a716-446655440003",
      "distanceKm": 45.2,
      "estimatedMinutes": 38
    }
  ],
  "totalDistanceKm": 365.7,
  "totalEstimatedMinutes": 305
}
```

**캐싱**: Redis (TTL: 10분)

**알고리즘**: Dijkstra 최단 경로

---

### 2. 허브 연결 목록 조회

**Endpoint**
```
GET /hub-connections
```

**권한**: `MANAGER`, `MASTER`

**Response** `200 OK`
```json
[
  {
    "connectionId": "990e8400-e29b-41d4-a716-446655440004",
    "departureHubId": "550e8400-e29b-41d4-a716-446655440000",
    "arrivalHubId": "660e8400-e29b-41d4-a716-446655440001",
    "distanceKm": 365.7,
    "estimatedMinutes": 305
  }
]
```

**캐싱**: Redis (TTL: 30분)

---

### 3. 허브 연결 상세 조회

**Endpoint**
```
GET /hub-connections/{hubConnectionId}
```

**권한**: `MANAGER`, `MASTER`

**Response** `200 OK`
```json
{
  "connectionId": "990e8400-e29b-41d4-a716-446655440004",
  "departureHubId": "550e8400-e29b-41d4-a716-446655440000",
  "arrivalHubId": "660e8400-e29b-41d4-a716-446655440001",
  "distanceKm": 365.7,
  "estimatedMinutes": 305
}
```

---

### 4. 허브 연결 생성

**Endpoint**
```
POST /hub-connections
```

**권한**: `MASTER`

**Request Body**
```json
{
  "departureHubId": "550e8400-e29b-41d4-a716-446655440000",
  "arrivalHubId": "660e8400-e29b-41d4-a716-446655440001",
  "distanceKm": 365.7,
  "estimatedMinutes": 305
}
```

**Response** `201 Created`
```json
"990e8400-e29b-41d4-a716-446655440004"
```

---

### 5. 허브 연결 수정

**Endpoint**
```
PUT /hub-connections/{hubConnectionId}
```

**권한**: `MASTER`

**Request Body**
```json
{
  "departureHubId": "550e8400-e29b-41d4-a716-446655440000",
  "arrivalHubId": "660e8400-e29b-41d4-a716-446655440001",
  "distanceKm": 370.0,
  "estimatedMinutes": 310
}
```

**Response** `200 OK`

---

### 6. 허브 연결 삭제

**Endpoint**
```
DELETE /hub-connections/{hubConnectionId}
```

**권한**: `MASTER`

**Response** `204 No Content`

---

## Delivery Service API

**Base Path**: `/deliveries`

**Port**: 9006

### 1. 배송 생성

**Endpoint**
```
POST /deliveries
```

**권한**: `MANAGER`, `MASTER`

**Request Body**
```json
{
  "orderId": "aa0e8400-e29b-41d4-a716-446655440005",
  "departureHubId": "550e8400-e29b-41d4-a716-446655440000",
  "arrivalHubId": "660e8400-e29b-41d4-a716-446655440001",
  "recipientName": "홍길동",
  "recipientAddress": "부산광역시 해운대구 센텀중앙로 79",
  "recipientPhone": "010-1234-5678"
}
```

**Response** `201 Created`
```json
{
  "deliveryId": "bb0e8400-e29b-41d4-a716-446655440006",
  "status": "PENDING"
}
```

---

### 2. 배송 조회

**Endpoint**
```
GET /deliveries/{deliveryId}
```

**권한**: `USER`, `MANAGER`, `MASTER`

**Response** `200 OK`
```json
{
  "deliveryId": "bb0e8400-e29b-41d4-a716-446655440006",
  "orderId": "aa0e8400-e29b-41d4-a716-446655440005",
  "status": "IN_TRANSIT",
  "currentHubId": "880e8400-e29b-41d4-a716-446655440003",
  "recipient": {
    "name": "홍길동",
    "address": "부산광역시 해운대구 센텀중앙로 79",
    "phone": "010-1234-5678"
  }
}
```

---

## Order Service API

**Base Path**: `/orders`

**Port**: 9001

### 1. 주문 생성

**Endpoint**
```
POST /orders
```

**권한**: `USER`, `MANAGER`, `MASTER`

**Request Body**
```json
{
  "productId": "cc0e8400-e29b-41d4-a716-446655440007",
  "quantity": 2,
  "supplierId": "dd0e8400-e29b-41d4-a716-446655440008",
  "receiverId": "ee0e8400-e29b-41d4-a716-446655440009",
  "deliveryAddress": "부산광역시 해운대구 센텀중앙로 79"
}
```

**Response** `201 Created`
```json
{
  "orderId": "aa0e8400-e29b-41d4-a716-446655440005",
  "status": "PENDING"
}
```

---

### 2. 주문 조회

**Endpoint**
```
GET /orders/{orderId}
```

**권한**: `USER`, `MANAGER`, `MASTER`

**Response** `200 OK`
```json
{
  "orderId": "aa0e8400-e29b-41d4-a716-446655440005",
  "status": "PREPARING",
  "productId": "cc0e8400-e29b-41d4-a716-446655440007",
  "quantity": 2,
  "totalPrice": 50000,
  "deliveryId": "bb0e8400-e29b-41d4-a716-446655440006"
}
```

---

## Company Service API

**Base Path**: `/companies`

**Port**: 9004

### 1. 업체 생성

**Endpoint**
```
POST /companies
```

**권한**: `MANAGER`, `MASTER`

**Request Body**
```json
{
  "name": "ABC 물류",
  "type": "SUPPLIER",
  "hubId": "550e8400-e29b-41d4-a716-446655440000",
  "address": "서울특별시 강남구 테헤란로 123",
  "contact": "02-1234-5678"
}
```

**Response** `201 Created`
```json
{
  "companyId": "dd0e8400-e29b-41d4-a716-446655440008"
}
```

---

## Product Service API

**Base Path**: `/products`

**Port**: 9005

### 1. 상품 생성

**Endpoint**
```
POST /products
```

**권한**: `MANAGER`, `MASTER`

**Request Body**
```json
{
  "name": "노트북",
  "description": "고성능 노트북",
  "price": 1500000,
  "stock": 100,
  "companyId": "dd0e8400-e29b-41d4-a716-446655440008"
}
```

**Response** `201 Created`
```json
{
  "productId": "cc0e8400-e29b-41d4-a716-446655440007"
}
```

---

## User Service API

**Base Path**: `/users`

**Port**: 9007

### 1. 사용자 등록

**Endpoint**
```
POST /users
```

**권한**: Public

**Request Body**
```json
{
  "username": "user123",
  "password": "password123!",
  "email": "user@example.com",
  "role": "USER"
}
```

**Response** `201 Created`
```json
{
  "userId": "ff0e8400-e29b-41d4-a716-446655440010"
}
```

---

## Auth Service API

**Base Path**: `/auth`

**Port**: 9009

### 1. 로그인

**Endpoint**
```
POST /auth/login
```

**권한**: Public

**Request Body**
```json
{
  "username": "user123",
  "password": "password123!"
}
```

**Response** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

### 2. 토큰 갱신

**Endpoint**
```
POST /auth/refresh
```

**Request Body**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```

---

## AI Service API

**Base Path**: `/ai`

**Port**: 9008

### 1. 배송 경로 분석

**Endpoint**
```
POST /ai/route-analysis
```

**권한**: `MANAGER`, `MASTER`

**Request Body**
```json
{
  "routeId": "770e8400-e29b-41d4-a716-446655440002",
  "trafficData": {
    "currentTime": "2025-11-13T14:00:00",
    "weatherCondition": "CLEAR"
  }
}
```

**Response** `200 OK`
```json
{
  "optimizedRoute": {
    "estimatedMinutes": 280,
    "recommendation": "현재 교통 상황을 고려할 때 경로 A가 25분 더 빠릅니다."
  }
}
```

---

## 에러 코드

### 공통 에러

| 코드 | HTTP Status | 설명 |
|------|-------------|------|
| `INVALID_INPUT` | 400 | 잘못된 입력 값 |
| `UNAUTHORIZED` | 401 | 인증 필요 |
| `FORBIDDEN` | 403 | 권한 없음 |
| `NOT_FOUND` | 404 | 리소스를 찾을 수 없음 |
| `CONFLICT` | 409 | 중복된 리소스 |
| `INTERNAL_SERVER_ERROR` | 500 | 서버 내부 오류 |

### Hub Service

| 코드 | HTTP Status | 설명 |
|------|-------------|------|
| `HUB_NOT_FOUND` | 404 | 허브를 찾을 수 없음 |
| `HUB_CODE_ALREADY_EXISTS` | 409 | 허브 코드가 이미 존재함 |

### Hub Route Service

| 코드 | HTTP Status | 설명 |
|------|-------------|------|
| `HUB_CONNECTION_NOT_FOUND` | 404 | 허브 연결을 찾을 수 없음 |
| `NO_VALID_ROUTE` | 404 | 유효한 경로가 없음 |

---

