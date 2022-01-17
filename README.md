### 기술스택
SpringBoot, JPA, gradle, h2, mysql, swagger

### 1. API 사양 설계
#### 비즈니스 요구사항 및 설계
 ```
 운영자(Admin)
 - (★)각 운영자는 여러개의 `상점`을 가질 수 있다.
 - 기본적으로는 `한국어 쇼핑몰`을 개설하고 필요에 따라서 `다국어 쇼핑몰`을 생성 할 수 있다.
 - 운영자의 아이디 및 이메일은 중복을 허용하지 않는다. (1 email 1 계정을 원칙)
  - 동시성 이슈를 위해 unique key로 보완한다.
 - 운영자의 아이디는 변경 할 수 없다.
 - 운영자의 휴대전화 번호/사업장 주소는 변경 할 수 있다.
 ```
 ```
 몰(Mall)
 - (★)한명의 운영자는 여러개의 쇼핑몰을 가질(운영 할) 수 있다.
 - 기본적으로는 한국어 쇼핑몰을 생성하고 선택에따라서 해외몰을 생성/운영 할 수 있다.
 - 동일한 국가코드의 몰을 중복으로 생성 할 수 없다.
 - 몰의 이름은 50자 제한이 있고, 중복 될 수 없다.
 - 몰에 진행중인 주문이 있는 경우 제거(폐업) 할 수 없다.
 ```

### 2. 도메인 설계
`각 테이블은 별도의 인조키를 두어서 비즈니스와의 연관성을 최대한 두지 않는다.`

``운영자(Admin)``
- 운영자 고유번호(pk)
- 몰 정보(양방향 관계)
- 운영자 아이디 (중복검사 및 제한문자 vaildation 적용)
- 운영자명
- 연락처 (vaildation 적용)
- 이메일 (vaildation 적용)
- 생성일 (LocalDateTime)
- 마지막 수정일 (LocalDateTime)

``몰(Mall) -> TODO``
- 상점 고유번호 (pk)
- 운영자 고유번호 (fk)
- 상점이름
- 지원국가코드
- 주소정보 -> embeded로 통합 (각 property는 오버라이딩해서 사업장 주소로 변경)
- 개설일 (LocalDateTime)
- 마지막 수정일 (LocalDateTime)

``쇼핑몰회원(member) -> TODO``
- 회원고유ID (pk)
- 상점 고유번호 (fk, 어느상점의 회원인가?)
- 회원아이디 (unique key, 중복검사 및 제한문자 vaildation 적용)
- 회원명
- 성별
- 생년월일
- 주소정보 -> embeded로 통합
- 회원상태 (ENUM)
- 회원가입일 (LocalDateTime)
- 회원정보 수정일 (LocalDateTime)

``상품 (product) -> TODO``

``상품카테고리 -> TODO``

``주문 -> TODO``
- 주문ID (pk)
- 회원아이디(fk)
- 배송아이디(fk)
- orderProducts (일대다)
- 배송일

``배송정보 -> TODO``

### 3. 도메인 개발
- DEFAULT DDL 작성
- Seed Data 작성
- repository, service layer 작성
- 도메인별 테스트 케이스 작성
- 컨트롤러 작성

### 4. Todo/Doing List
- `완료` 수정 API를 실행 할 때, 객체타입을 지닌 엔티티 처리 방식 확인 필요
    - 엔티티는 `@DynamicUpdate`를 통해서 변경된 항목만 수정하도록하고, 복합 값 타입은 객체를 불변객체로 생성하고 수정 시, 객체 자체를 재생성하여 등록하도록 수정
- `완료` admin api controller mockMvc 테스트 케이스 작성
- `완료` GET /api/admins -> pagenation 적용 필요
- `완료` 몰(Mall) Service Layer 테스트케이스 작성
- `진행중 (hold)` Pageable Validation 처리
  - 유효하지 않는 파라미터 처리 -> 제외하고 값 응답
- `완료` 몰(Mall) api 설계 필요
- `진행중` 상품/카테고리/주문 도메인 설계

### 5. 백로그
- Spring Rest Docs 적용하기
  - swagger에 비해서 장단점이 있음 (https://techblog.woowahan.com/2597/)