## 1. Service Scenario 
### e-커머스 서비스 

## 2. Milestone
### 1️⃣ 1주차: 시나리오 분석 및 프로젝트 준비 (10/5 ~ 10/11)
- **시나리오 분석 및 요구사항 정의:**
    - 개발 환경 준비
        - Architecture, DB ORM, Test
- **시나리오 분석 및 요구사항 정의:**
  - 각 API의 요구사항 분석 및 시퀀스 다이어그램, 플로우차트 작성

- **ERD 설계:**
  - `User`, `Product`, `Order`, `Cart` 등의 도메인 분석 및 ERD 정의

- **API 명세 및 Mock API 설계:**
  - 잔액 충전/조회, 상품 조회, 주문/결제, 상위 상품 조회, 장바구니 기능에 대한 API 명세서 작성
  - 각 API의 URL, 요청/응답 형식, 에러 케이스 정의

- **초기 Mock API 작성:**
  - 각 API의 Mock 버전을 간단히 구현하여 프론트엔드에서 사용할 수 있도록 제공
  - 프로젝트 기본 패키지 구조 및 설정 완료

---

### 2️⃣ 2주차: 잔액 충전 / 조회 API 개발 (10/12 ~ 10/18)

- **API 개발:**
  - `POST /balance/charge`: 사용자 ID와 금액을 받아 잔액을 충전하는 기능 개발
  - `GET /balance`: 사용자 ID를 통해 현재 잔액 조회

- **동시성 제어 및 트랜잭션 적용:**
  - **Pessimistic Locking** 또는 **Optimistic Locking** 적용하여 동시성 문제 해결
  - 여러 요청이 동시에 들어올 때 데이터가 일관성 있게 관리되도록 처리

- **단위 테스트 작성:**
  - 잔액 부족, 음수 금액 충전, 잘못된 사용자 ID 처리 및 검증

---

### 3️⃣ 3주차: 상품 조회 API 및 재고 관리 로직 개발 (10/19 ~ 10/25)

- **API 개발:**
  - `GET /products`: 상품 ID, 이름, 가격, 잔여 수량을 포함한 상품 목록 반환

- **재고 관리 로직 개발:**
  - 실시간 재고 관리 로직 개발 (상품 수량이 정확하게 조회 및 관리)

- **테스트 시나리오 작성:**
  - 다수 사용자가 동시에 상품을 조회할 때 재고 정보가 정확하게 조회되는지 검증
  - 재고가 부족할 때의 예외 처리 및 데이터 일관성 테스트

---

### 4️⃣ 4주차: 주문 / 결제 API 개발 및 데이터 플랫폼 연동 (10/26 ~ 11/1)

- **API 개발:**
  - `POST /order`: 사용자 ID와 (상품 ID, 수량) 목록을 받아 주문 생성 및 결제 수행
  - 주문 생성 시, 사용자 잔액 차감 및 상품 재고 감소 로직 구현

- **동시성 제어 및 트랜잭션 처리:**
  - 다수의 주문 요청이 동시에 발생해도 트랜잭션과 락을 통해 데이터 정합성 유지

- **데이터 플랫폼 연동 (Kafka, Spark):**
  - 주문 성공 시, Kafka를 통해 주문 데이터를 데이터 플랫폼으로 전송
  - Spark를 통해 실시간 통계 데이터 생성 및 관리

- **통합 테스트:**
  - 주문 및 결제 시나리오 테스트, 데이터 플랫폼 연동 검증

---

### 5️⃣ 5주차: 상위 판매 상품 조회 API 및 장바구니 기능 개발 (11/2 ~ 11/8)

- **상위 판매 상품 조회 API 개발:**
  - `GET /products/top`: 최근 3일간 가장 많이 팔린 상위 5개 상품 조회
  - 판매 통계 데이터를 기반으로 상위 상품을 정확하게 정렬 및 반환

- **장바구니 기능 개발:**
  - `POST /cart/add`: 장바구니에 상품 추가
  - `DELETE /cart/remove`: 장바구니에서 상품 제거
  - `GET /cart`: 장바구니에 담긴 상품 목록 조회

- **장바구니와 주문 연동:**
  - 장바구니의 상품을 주문할 때, 장바구니와 주문 상태가 정합성 있게 유지되도록 설계
  - 전체 시스템 통합 테스트 및 성능 최적화

---
## 시나리오 요구사항 분석
### 1.잔액 충전 / 조회 API
```mermaid
flowchart TD
    %% 시작 지점
    A[Start] --> B{요청 유형 선택}

    %% 잔액 충전 프로세스
    B -->|충전 요청| C[사용자 ID 및 충전 금액 확인]
    C --> D{사용자 ID 유효성 검증}
    D -->|유효| E[충전 금액 계산]
    D -->|유효하지 않음| F[오류 반환: 사용자 ID 불일치]

    E --> G[잔액 충전 트랜잭션 시작]
    G --> H["동시성 제어 - 락 사용"]
    H --> I[현재 잔액에 충전 금액 추가]
    I --> J[충전 완료 후 잔액 업데이트]
    J --> K{충전 성공 여부}
    K -->|성공| L[잔액 충전 성공 응답 반환]
    K -->|실패| M[오류 반환: 충전 실패]

    %% 잔액 조회 프로세스
    B -->|조회 요청| N[사용자 ID 확인]
    N --> O{사용자 ID 유효성 검증}
    O -->|유효| P[현재 잔액 조회]
    O -->|유효하지 않음| F

    P --> Q{잔액 조회 성공 여부}
    Q -->|성공| R[잔액 정보 반환]
    Q -->|실패| M

    %% 트랜잭션 종료 및 흐름 마무리
    L --> S[트랜잭션 종료]
    R --> S
    M --> S
    F --> S
    S --> T[End]

    %% 동시성 제어 처리
    H --> U{"트랜잭션 중 다른 요청이 접근?"}
    U -->|예| V[동시성 충돌: 트랜잭션 무효화 후 실패 반환]
    U -->|아니오| I

```

### 2.상품 조회 API
```mermaid
sequenceDiagram
    participant User as 사용자
    participant APIGateway as API Gateway
    participant ProductService as Product Service
    participant Cache as Cache
    participant Database as Database

    User->>APIGateway: 상품 조회 요청
    APIGateway->>ProductService: 상품 정보 조회 요청
    ProductService->>Cache: 캐시에서 상품 이름, 가격 조회
    alt 캐시 히트 (이름, 가격)
        Cache-->>ProductService: 캐시된 상품 정보 (이름, 가격)
    else 캐시 미스
        ProductService->>Database: 이름, 가격 조회
        Database-->>ProductService: 상품 이름, 가격 반환
        ProductService->>Cache: 캐시 업데이트 (이름, 가격)
    end
    ProductService->>Database: 잔여 수량 조회 (실시간 정합성 보장)
    Database-->>ProductService: 잔여 수량 반환
    ProductService-->>APIGateway: 상품 정보 전달 (ID, 이름, 가격, 잔여수량)
    APIGateway-->>User: 상품 정보 반환 (ID, 이름, 가격, 잔여수량)

```

### 3.주문 / 결제 API
```mermaid
sequenceDiagram
    actor User
    participant APIGateway as API Gateway
    participant OrderService as Order Service
    participant UserService as User Service
    participant ProductService as Product Service
    participant Database as Database
    participant DataPlatform as 외부 데이터 플랫폼

    User->>APIGateway: 🛒 주문 요청 (사용자 ID, 상품 ID, 수량 목록)
    APIGateway->>OrderService: 주문 요청 전달 (사용자 ID, 상품 ID, 수량 목록)

    note right of OrderService: 주문 요청 처리 시작

    OrderService->>UserService: 사용자 잔액 확인 요청 (사용자 ID)
    UserService->>Database: 사용자 잔액 조회
    Database-->>UserService: 사용자 잔액 반환
    UserService-->>OrderService: 잔액 정보 반환

    alt 잔액이 충분할 경우
        OrderService->>ProductService: 상품 재고 확인 요청 (상품 ID, 주문 수량)
        ProductService->>Database: 상품 재고 조회
        Database-->>ProductService: 재고 정보 반환
        ProductService-->>OrderService: 재고 확인 결과 반환

        alt 재고가 충분할 경우
            OrderService->>UserService: 잔액 차감 요청 (사용자 ID, 결제 금액)
            UserService->>Database: 잔액 업데이트 (잔액 차감)
            Database-->>UserService: 잔액 업데이트 결과 반환
            UserService-->>OrderService: 잔액 차감 완료

            OrderService->>ProductService: 재고 감소 요청 (상품 ID, 주문 수량)
            ProductService->>Database: 재고 업데이트
            Database-->>ProductService: 재고 업데이트 결과 반환
            ProductService-->>OrderService: 재고 감소 완료

            OrderService->>Database: 주문 정보 저장 (주문 ID, 사용자 ID, 상품 목록, 결제 금액, 주문 상태)
            Database-->>OrderService: 주문 정보 저장 완료

            OrderService->>DataPlatform: 주문 정보 전송 (주문 ID, 사용자 ID, 결제 금액)
            DataPlatform-->>OrderService: 전송 성공

            OrderService-->>APIGateway: 주문 및 결제 성공
            APIGateway-->>User: 🎉 결제 성공 및 주문 완료
        else 재고가 부족할 경우
            OrderService-->>APIGateway: 🚫 재고 부족으로 주문 실패
            APIGateway-->>User: ❌ 주문 실패 (재고 부족)
        end
    else 잔액이 부족할 경우
        OrderService-->>APIGateway: 💸 잔액 부족으로 결제 실패
        APIGateway-->>User: ❌ 결제 실패 (잔액 부족)
    end
```
---
