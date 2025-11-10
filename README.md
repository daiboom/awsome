# Awsome - User Management REST API

Spring Boot 기반 사용자 관리 시스템 REST API 프로젝트입니다.

## 📋 프로젝트 소개

JPA, HATEOAS, 페이지네이션, 필터링, Swagger 문서화를 포함한 RESTful API 프로젝트입니다.

### 주요 기능

- ✅ JPA를 활용한 사용자 CRUD 작업
- ✅ HATEOAS를 통한 하이퍼미디어 링크 제공
- ✅ 페이지네이션 및 필터링 (이름, 날짜 범위)
- ✅ Swagger/OpenAPI를 통한 API 문서화
- ✅ 다국어 지원 (한국어, 일본어, 프랑스어)
- ✅ 버전 관리 API (V1, V2)
- ✅ 예외 처리 및 커스텀 에러 응답
- ✅ H2 인메모리 데이터베이스

## 🛠 기술 스택

- **Java**: 17
- **Spring Boot**: 3.5.7
- **Spring Data JPA**: 데이터베이스 연동
- **H2 Database**: 인메모리 데이터베이스
- **Spring HATEOAS**: 하이퍼미디어 링크
- **Swagger/OpenAPI**: API 문서화
- **Lombok**: 보일러플레이트 코드 감소
- **Maven**: 빌드 도구

## 📦 사전 요구사항

- Java 17 이상
- Maven 3.6 이상 (또는 프로젝트에 포함된 Maven Wrapper 사용)

## 🚀 빌드 방법

### Maven Wrapper 사용 (권장)

프로젝트에 Maven Wrapper가 포함되어 있어 별도의 Maven 설치 없이 빌드할 수 있습니다.

#### Windows

```bash
.\mvnw.cmd clean install
```

#### macOS / Linux

```bash
./mvnw clean install
```

### Maven 설치된 경우

```bash
mvn clean install
```

### 빌드 옵션

- **테스트 제외하고 빌드**: `mvn clean install -DskipTests`
- **JAR 파일만 생성**: `mvn clean package`
- **의존성 다운로드**: `mvn dependency:resolve`

## ▶️ 실행 방법

### 방법 1: Maven Wrapper 사용 (권장)

#### Windows

```bash
.\mvnw.cmd spring-boot:run
```

#### macOS / Linux

```bash
./mvnw spring-boot:run
```

### 방법 2: Maven 사용

```bash
mvn spring-boot:run
```

### 방법 3: JAR 파일 실행

먼저 빌드를 수행한 후:

```bash
# 빌드
mvn clean package

# 실행
java -jar target/awsome-0.0.1-SNAPSHOT.jar
```

### 방법 4: IDE에서 실행

1. `AwsomeApplication.java` 파일을 열기
2. `main` 메서드를 찾아 실행
3. 또는 IDE의 Run/Debug 기능 사용

## 🌐 접속 정보

애플리케이션 실행 후 다음 URL로 접속할 수 있습니다:

- **애플리케이션**: http://localhost:8088
- **Swagger UI**: http://localhost:8088/swagger-ui.html
- **API Docs**: http://localhost:8088/v3/api-docs
- **H2 Console**: http://localhost:8088/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (비워두기)

## 📡 주요 API 엔드포인트

### 사용자 관리 (JPA)

- `GET /jpa/users` - 사용자 목록 조회 (페이지네이션, 필터링 지원)
  - Query Parameters:
    - `page`: 페이지 번호 (기본값: 0)
    - `size`: 페이지 크기 (기본값: 10)
    - `name`: 이름 필터 (부분 일치)
    - `startDate`: 시작 날짜 (yyyy-MM-dd 형식)
    - `endDate`: 종료 날짜 (yyyy-MM-dd 형식)
- `GET /jpa/users/{id}` - 사용자 조회
- `POST /jpa/users` - 사용자 생성
- `PATCH /jpa/users/{id}` - 사용자 수정
- `DELETE /jpa/users/{id}` - 사용자 삭제

### 사용자 관리 (DAO)

- `GET /users` - 사용자 목록 조회
- `GET /users/{id}` - 사용자 조회
- `POST /users` - 사용자 생성
- `DELETE /users/{id}` - 사용자 삭제

### 관리자 API

- `GET /admin/users` - 관리자용 사용자 목록 조회 (V1, V2)

## 📝 API 사용 예시

### 페이지네이션

```bash
# 첫 번째 페이지, 10개씩
GET http://localhost:8088/jpa/users?page=0&size=10

# 두 번째 페이지, 5개씩
GET http://localhost:8088/jpa/users?page=1&size=5
```

### 필터링

```bash
# 이름으로 필터링
GET http://localhost:8088/jpa/users?name=홍길동&page=0&size=10

# 날짜 범위로 필터링
GET http://localhost:8088/jpa/users?startDate=2024-01-01&endDate=2024-12-31&page=0&size=10

# 이름 + 날짜 범위 필터링
GET http://localhost:8088/jpa/users?name=홍&startDate=2024-01-01&endDate=2024-12-31&page=0&size=10
```

### 사용자 생성

```bash
POST http://localhost:8088/jpa/users
Content-Type: application/json

{
  "name": "홍길동",
  "joinDate": "2024-01-01T00:00:00.000+00:00"
}
```

## 🗄 데이터베이스

프로젝트는 H2 인메모리 데이터베이스를 사용합니다. 애플리케이션 시작 시 `data.sql`과 `test.sql` 파일이 자동으로 실행되어 초기 데이터가 생성됩니다.

### H2 Console 접속

1. 애플리케이션 실행
2. http://localhost:8088/h2-console 접속
3. 다음 정보 입력:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (비워두기)

## 🧪 테스트

```bash
# 모든 테스트 실행
mvn test

# 특정 테스트 클래스 실행
mvn test -Dtest=AwsomeApplicationTests
```

## 📚 프로젝트 구조

```
src/
├── main/
│   ├── java/
│   │   └── org/startlight/awsome/
│   │       ├── AwsomeApplication.java      # 메인 애플리케이션
│   │       ├── bean/                       # 엔티티 클래스
│   │       ├── controller/                # REST 컨트롤러
│   │       ├── repository/                 # JPA 리포지토리
│   │       ├── dto/                        # 데이터 전송 객체
│   │       ├── exception/                 # 예외 처리
│   │       └── config/                    # 설정 클래스
│   └── resources/
│       ├── application.yml                 # 애플리케이션 설정
│       ├── data.sql                        # 초기 데이터
│       └── i18n/                          # 다국어 메시지
└── test/
    └── java/                              # 테스트 코드
```

## 🔧 설정

주요 설정은 `src/main/resources/application.yml` 파일에서 관리됩니다.

- **서버 포트**: 8088
- **데이터베이스**: H2 인메모리
- **로깅 레벨**: DEBUG

## 📄 라이선스

이 프로젝트는 개인 학습 및 개발 목적으로 작성되었습니다.

## 👤 작성자

Startlight(Daiboom)
