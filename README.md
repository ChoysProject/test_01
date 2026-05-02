🚀 [모의 과제] 외부 거래소 연계 API 개발 및 장애 복원력 구현
1. 과제 배경

귀하는 증권사 대외연계망 시스템의 백엔드 개발자입니다.

클라이언트(앱/웹)로부터 '주식 매수 주문' 요청을 받아, 외부 시스템인 '한국거래소(가상)' API를 호출하여 주문을 체결하고 결과를 반환하는 시스템을 구축해야 합니다.

2. 핵심 요구사항 (기능)

주문 접수 API 구현 (POST /api/v1/orders):

요청 파라미터: userId, stockCode(종목코드), quantity(수량), price(가격)

응답: 성공/실패 여부 및 주문 번호

외부 API 연동 (POST http://localhost:9090/external/krx/order):

우리 서버가 클라이언트의 요청을 받으면, 위 주소(가상의 거래소 서버)로 HTTP 요청을 보내야 합니다.

주문 이력 저장:

요청된 주문의 상태(요청, 체결완료, 체결실패)를 데이터베이스(H2)에 저장해야 합니다.

3. 🌟 제약사항 및 시니어 평가 포인트 (가장 중요)

타임아웃(Timeout) 처리: 외부 거래소 API는 응답이 매우 느릴 때가 있습니다. 호출 후 2초 이상 응답이 없으면 즉시 연결을 끊고 클라이언트에게 '주문 지연(실패)' 응답을 내려야 합니다. 우리 서버의 스레드가 무한정 대기해서는 안 됩니다.

서킷 브레이커(Circuit Breaker) 적용: 외부 거래소 API가 지속적으로 타임아웃을 발생시키거나 500 Error를 연속해서 뱉어낼 경우, 우리 서버는 외부 API 호출을 잠시 차단(Open 상태)하고, 외부 시스템이 정상화될 때까지 클라이언트에게 즉시 Fallback 응답(예: "현재 거래소 연결이 지연되고 있습니다")을 반환해야 합니다.

예외 처리: 외부 연계 실패 시, H2 DB에 저장된 주문 상태가 정확히 '실패'로 기록(또는 롤백)되어야 합니다.

4. 기술 스택 제안

Core: Java 17+, Spring Boot 3.x

DB: H2 (In-memory), Spring Data JPA

Resilience: Resilience4j (CircuitBreaker, TimeLimiter)

HTTP Client: RestTemplate 또는 WebClient 또는 RestClient

🗺️ 진행 프로세스 제안
한 번에 다 하려고 하면 막막하니, 다음과 같이 단계를 나누어 진행하는 것을 추천합니다.

Step 1: GitHub 레포지토리 생성 및 Spring Boot 프로젝트 초기 세팅 (build.gradle 의존성 추가, H2 설정, 공통 응답 DTO 및 예외 처리 클래스 생성)

Step 2: 외부 통신용 가짜 API(Mock 서버) 세팅 및 도메인 로직 구현

Step 3: Resilience4j 적용 (타임아웃 및 서킷 브레이커 설정) 및 테스트

지금 바로 시작하기 위해, Step 1에 필요한 build.gradle 의존성 목록과 프로젝트 패키지 구조 뼈대부터 먼저 뽑아드릴까요?
