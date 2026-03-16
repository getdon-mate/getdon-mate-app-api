# getdon-mate-app-api

모임 통장 관리 서비스 REST API

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.4.3 |
| ORM | Spring Data JPA |
| Query | QueryDSL 5.1.0 |
| Security | Spring Security + JWT (jjwt 0.12.6) |
| Build | Gradle 9.3.1 |
| DB (운영) | PostgreSQL |
| DB (테스트) | H2 (in-memory) |
| Docs | Spring REST Docs + restdocs-api-spec (OpenAPI 3) |
| Infra | Docker, OCI, GitHub Actions |

---

## 프로젝트 구조

```
src/main/java/com/api/app/getdonapi/
├── global/          # 공통 설정, JWT, 응답/예외 처리
├── meeting/         # 모임 통장 도메인
├── meetingmember/   # 모임 멤버 도메인
├── member/          # 회원 도메인
├── deposit/         # 입금 도메인
├── withdrawal/      # 출금 도메인
├── paymenthistory/  # 납부 이력 도메인
├── onetimepayment/  # 일시납 도메인
└── automaticpayment/ # 자동납 도메인
```

---

## 빌드 및 실행

```bash
# 의존성 다운로드
./gradlew dependencies

# 빌드 (테스트 제외)
./gradlew build -x test

# 빌드 (테스트 포함)
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

---

## 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "com.api.app.getdonapi.meeting.service.query.MeetingQueryServiceImplTest"

# 테스트 결과 확인
open build/reports/tests/test/index.html
```

### 테스트 구성

| 레이어 | 방식 | 설명 |
|--------|------|------|
| Controller | `RestDocsSupport` (MockMvc) | API 문서 자동 생성 |
| Service (단위) | Mockito | Repository mock 처리 |
| Service (통합) | `@DataJpaTest` + H2 | 실제 DB 연동 검증 |

---

## API 문서 (OpenAPI 3)

```bash
# 테스트 실행 후 OpenAPI yaml 생성
./gradlew openapi3

# 생성 경로
src/main/resources/static/docs/openapi3.yaml
```

---

## CI/CD

- **트리거**: `main` 브랜치 push
- **파이프라인**: `openapi3` → `build jar` → `Docker build & push` → OCI 배포

### 필요한 GitHub Secrets

| Secret | 설명 |
|--------|------|
| `DOCKER_USERNAME` | Docker Hub 사용자명 |
| `DOCKER_PASSWORD` | Docker Hub 비밀번호 |
| `OCI_HOST` | OCI 서버 호스트 |
| `OCI_USERNAME` | OCI SSH 사용자명 |
| `OCI_SSH_KEY` | OCI SSH 개인키 |
| `DB_URL` | 운영 DB URL |
| `DB_USERNAME` | 운영 DB 사용자명 |
| `DB_PASSWORD` | 운영 DB 비밀번호 |
| `JWT_SECRET_KEY` | JWT 서명 키 (256bit 이상) |