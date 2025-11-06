# Sparta Logistics MSA Project

Spring Boot 기반의 마이크로서비스 아키텍처(MSA) 물류 관리 시스템입니다.

## 📋 프로젝트 구조

```
sparta_logistics/
├── infra/                      # 인프라 관련 모듈
│   └── eureka-server/         # Netflix Eureka 서비스 레지스트리
├── services/                   # 비즈니스 서비스 모듈
│   └── user-server/           # 사용자 관리 서비스
├── build.gradle               # 루트 빌드 설정 (공통 설정)
├── settings.gradle            # 멀티모듈 구성
├── gradlew / gradlew.bat      # Gradle Wrapper
└── README.md
```

## 🛠️ 기술 스택

- **Java**: 17
- **Spring Boot**: 3.5.7
- **Spring Cloud**: 2025.0.0
- **Build Tool**: Gradle 8.10
- **Service Discovery**: Netflix Eureka
- **Configuration**: Spring Dotenv

## 🚀 시작하기

### 사전 요구사항

- JDK 21 이상
- Gradle 8.x (또는 포함된 Gradle Wrapper 사용)

### 🚨 빌드 전 체크리스트

빌드 오류를 방지하기 위해 다음을 확인하세요:

1. ✅ `settings.gradle` 파일명 확인 (~~setting.gradle~~ ❌)
2. ✅ 서브모듈에 독립적인 gradle 폴더가 없어야 함
3. ✅ 루트에만 `gradlew`, `gradlew.bat` 존재
4. ✅ Java 21 설치 확인: `java -version`

### 프로젝트 빌드

#### Windows 사용자

**방법 1: 간편 스크립트 사용 (추천)**
```bash
# 대화형 빌드 메뉴
build-quick.bat

# 모든 서비스 한번에 시작
start-all.bat
```

**방법 2: 직접 명령어 실행**
```bash
# 전체 프로젝트 빌드 (테스트 포함)
gradlew.bat clean build

# 빠른 빌드 (테스트 제외) - 추천
gradlew.bat clean build -x test

# 특정 모듈만 빌드
gradlew.bat :infra:eureka-server:build
gradlew.bat :services:user-server:build
```

#### Linux/Mac 사용자

```bash
# 전체 프로젝트 빌드
./gradlew clean build

# 테스트 제외하고 빌드 (더 빠름)
./gradlew clean build -x test

# 특정 모듈만 빌드
./gradlew :infra:eureka-server:build
./gradlew :services:user-server:build

# 병렬 빌드 (속도 향상)
./gradlew clean build -x test --parallel
```

### 프로젝트 실행

#### ⚡ 빠른 시작 (Windows)

```bash
# 모든 서비스 자동 시작 (권장)
start-all.bat
```

이 스크립트는:
1. Eureka Server를 먼저 시작 (8761 포트)
2. 30초 대기 (Eureka 준비 시간)
3. User Service 시작 (8081 포트)
4. 각 서비스는 별도 콘솔 창에서 실행

#### 🔧 수동 실행 (모든 OS)

**터미널 1: Eureka Server**
```bash
# Windows
gradlew.bat :infra:eureka-server:bootRun

# Linux/Mac
./gradlew :infra:eureka-server:bootRun
```

**터미널 2: User Service** (30초 후)
```bash
# Windows
gradlew.bat :services:user-server:bootRun

# Linux/Mac
./gradlew :services:user-server:bootRun
```

#### 📦 JAR 파일로 실행

```bash
# 1. 먼저 빌드
./gradlew clean build -x test

# 2. JAR 실행
java -jar infra/eureka-server/build/libs/eureka-server-0.0.1-SNAPSHOT.jar
java -jar services/user-server/build/libs/user-server-0.0.1-SNAPSHOT.jar
```

#### 🌐 접속 확인

- **Eureka Dashboard**: http://localhost:8761
- **User Service**: http://localhost:8081
- User Service가 Eureka에 등록되었는지 Dashboard에서 확인

## 📦 모듈 설명

### 1. infra/eureka-server
- **역할**: 서비스 디스커버리 및 레지스트리
- **포트**: 8761 (기본)
- **설명**: 모든 마이크로서비스가 등록되고 서로를 찾을 수 있게 해주는 중앙 레지스트리

### 2. services/user-server
- **역할**: 사용자 관리 서비스
- **포트**: 8080 (기본, 설정에 따라 변경 가능)
- **설명**: 사용자 인증, 권한 관리 등 사용자 관련 기능 제공

## 🔧 새로운 서비스 추가하기

1. **services 디렉토리에 새 모듈 생성**
   ```bash
   mkdir -p services/new-service/src/main/java
   mkdir -p services/new-service/src/main/resources
   mkdir -p services/new-service/src/test/java
   ```

2. **settings.gradle에 모듈 추가**
   ```gradle
   include 'services:new-service'
   ```

3. **모듈의 build.gradle 작성**
   ```gradle
   description = 'New Service Description'
   
   dependencies {
       implementation 'org.springframework.boot:spring-boot-starter-web'
       implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
       // 추가 의존성...
   }
   ```

4. **메인 클래스 및 설정 파일 작성**

## 📝 멀티모듈 구조의 장점

✅ **코드 재사용성**: 공통 설정과 의존성을 루트에서 관리  
✅ **일관성**: 모든 서비스가 동일한 Java, Spring 버전 사용  
✅ **빌드 효율성**: 단일 명령으로 전체 프로젝트 빌드  
✅ **버전 관리**: 하나의 Git 저장소로 모든 서비스 관리  
✅ **개발 편의성**: IDE에서 모든 서비스를 한 번에 열어 작업 가능

## 🐛 트러블슈팅

### ❌ `Could not find method implementation()`

**원인**: Gradle 플러그인이 서브모듈에 제대로 적용되지 않음

**해결방법**:
1. `settings.gradle` 파일명 확인 (~~setting.gradle~~ 아님!)
2. 서브모듈에서 불필요한 gradle 관련 파일 삭제:
   ```bash
   # 각 서브모듈에서 삭제해야 할 파일들
   - gradle/
   - .gradle/
   - gradlew
   - gradlew.bat
   - settings.gradle
   ```
3. 루트 `build.gradle`에서 infra, services 폴더 제외 처리 확인

### ❌ `Task 'wrapper' not found in project ':infra:eureka-server'`

**원인**: wrapper 태스크는 루트 프로젝트에만 존재

**해결방법**:
```bash
# ❌ 잘못된 사용
./gradlew :infra:eureka-server:wrapper

# ✅ 올바른 사용
./gradlew wrapper
./gradlew wrapper --gradle-version 8.10
```

### ❌ 빌드 시 테스트 실패

**원인**: Eureka 관련 테스트가 실제 Eureka Server 연결 시도

**해결방법 1**: 테스트 스킵 (빠른 빌드)
```bash
./gradlew build -x test
```

**해결방법 2**: 테스트 설정 수정 (이미 적용됨)
```java
@SpringBootTest
@TestPropertySource(properties = {
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false"
})
class ApplicationTests {
    @Test void contextLoads() {}
}
```

### ❌ Java 버전 오류

**증상**: `Unsupported class file major version` 또는 `invalid target release`

**해결방법**:
```bash
# Java 버전 확인
java -version

# JDK 21이 설치되어 있는지 확인
# 없다면 https://adoptium.net/ 에서 다운로드
```

### ❌ 포트 충돌

**증상**: `Port 8761 was already in use` 또는 `Address already in use`

**해결방법**:
```bash
# Windows: 포트 사용 프로세스 확인 및 종료
netstat -ano | findstr :8761
taskkill /PID [프로세스ID] /F

# Linux/Mac
lsof -i :8761
kill -9 [PID]
```

### 🔍 디버깅 명령어

```bash
# 프로젝트 구조 확인
./gradlew projects

# 사용 가능한 태스크 목록
./gradlew tasks

# 특정 모듈의 태스크
./gradlew :infra:eureka-server:tasks

# 의존성 확인
./gradlew :infra:eureka-server:dependencies

# 상세 로그로 빌드
./gradlew build --info --stacktrace
```

## 🏗️ 아키텍처 설계 원칙

### 디렉토리 구조
- **infra/**: 인프라 관련 서비스 (Eureka, Config Server, Gateway 등)
- **services/**: 비즈니스 로직 서비스 (User, Order, Delivery 등)

### 공통 설정 (루트 build.gradle)
- Spring Boot, Spring Cloud 버전 관리
- Java 버전 통일 (Java 21)
- 공통 의존성 (Lombok, Dotenv)
- BOM(Bill of Materials)을 통한 의존성 버전 관리

### 개별 모듈 설정 (각 서비스의 build.gradle)
- 서비스별 특화 의존성만 추가
- 최소한의 설정으로 유지

## 🔍 주요 파일 설명

### settings.gradle
```gradle
rootProject.name = 'msa-logistics-project'
include 'infra:eureka-server'
include 'services:user-server'
```
- 프로젝트에 포함될 모듈을 정의

### 루트 build.gradle
- `apply false`: 루트 프로젝트에는 플러그인 적용하지 않음
- `subprojects {}`: 모든 하위 모듈에 공통 설정 적용
- 폴더 구조용 디렉토리(`infra`, `services`) 제외 처리

## 📋 유용한 명령어 모음

### 빌드 관련
```bash
./gradlew clean                    # 빌드 결과물 삭제
./gradlew build                    # 전체 빌드 (테스트 포함)
./gradlew build -x test            # 빠른 빌드 (테스트 제외)
./gradlew build --parallel         # 병렬 빌드 (속도 향상)
./gradlew build --no-daemon        # 데몬 없이 빌드
```

### 실행 관련
```bash
./gradlew :infra:eureka-server:bootRun          # Eureka 실행
./gradlew :services:user-server:bootRun         # User Service 실행
./gradlew bootRun --args='--spring.profiles.active=dev'  # 프로파일 지정
```

### JAR 관련
```bash
./gradlew :infra:eureka-server:bootJar          # JAR 생성
java -jar infra/eureka-server/build/libs/eureka-server-0.0.1-SNAPSHOT.jar
```

### 테스트 관련
```bash
./gradlew test                                  # 전체 테스트
./gradlew :services:user-server:test           # 특정 모듈 테스트
./gradlew test --tests UserServiceTest         # 특정 테스트 실행
```

### 정보 확인
```bash
./gradlew projects                              # 프로젝트 구조
./gradlew tasks                                 # 전체 태스크 목록
./gradlew dependencies                          # 의존성 트리
./gradlew -v                                    # Gradle 버전
```

## 🤝 기여 방법

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이센스

이 프로젝트는 MIT 라이센스를 따릅니다.

## 📧 연락처

프로젝트 관련 문의사항이 있으시면 이슈를 등록해주세요.

---

**Made with ❤️ by Sparta Team**