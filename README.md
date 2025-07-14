# spring-gift-product

### API 설계
| 메소드    | URL               | request          | response         | 기능     |
|--------|-------------------|------------------|------------------|--------|
| POST   | /api/products     | CreateProductDto | 201(created)     | 생성     |
| GET    | api/products/{id} | -                | ProductDto       | 단건조회   |
| GET    | api/products      | page-parameter   | List<ProductDto> | 전체조회   |
| PATCH  | api/products/{id} | UpdateProductDto | ProductDto       | 일부수정   |
| DELETE | api/products/{id} | -                | 204(no-content)  | 단건조회   |

---

# 1단계 상품관리

### step1 구현 기능 목록
- ProductCollector 구현
- ProductCollector 테스트 작성
- 상품 생성 기능 구현
- 상품 조회 기능 구현
- 상품 수정 기능 구현
- 상품 삭제 기능 구현
- API 테스트 작성

### step1 개선 목록
- Validator 패턴 적용 및 예외처리 응답 코드 개선
- ProductRepository(ProductCollector) 동시성 제어 ConcurrentHashMap 적용
- Product 도메인 객체 캡슐화, 불변객체로 변경, builder 패턴 적용
- controller 통합 테스트 (CRUD 정상 작동 시나리오) 구현

### step2 구현 기능 목록
- 관리자 화면(홈) 구현
- 상품 생성 화면 구현
- 단일 상품 조회, 수정 화면 구현

### step3 구현 기능 목록
- h2database 구동 설정 및 스키마, 데이터 파일 추가
- ProductRepository를 JdbcClient로 변경

---

# 2단계 위시 리스트

### step0 기본 코드준비
- 상품 관리 코드 옮겨오기
- 코드 클리닝 및 schema.sql 생성문 개선
- DB insert 예외처리 추가
- 테스트 코드 개선

### step1 유효성 검사 및 예외처리
- 정규표현식으로 상품 이름을 검증하는 ProductValidator 추가
- ProductService 상품 검증 로직 추가
- 잘못된 이름 검증 테스트 코드 추가
- 관리자 페이지 응답 메세지 출력 수정
- 도메인 팩토리 메서드 적용 및 검증로직 추가
- 상품 상태 필드 추가 및 관련 로직 수정
- 조회 옵션 적용, 관리 페이지에서 판매중인 상품만 보이도록 수정
- feature-rule.md 구현 명세 문서 추가

### step2 회원 로그인
- member 테이블, 도메인, 저장소 구현
- 회원가입 로직 추가
- JWT 인증 로그인 추가
- JwtInterceptor, WebConfig 추가

### step3 위시 리스트
- 위시리스트 테이블, 도메인, 레파지토리 구현
- CurrentMember 어노테이션 ArgumentResolver 구현
- 위시 리스트 상품 추가 기능 추가
- 사용자 별 위시 리스트 조회 기능 추가
- 위시 리스트 상품 삭제 기능 추가