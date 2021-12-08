### 기술스택
SpringBoot, JPA, gradle, h2, swagger

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
 - 몰의 이름은 50자 제한이 있고, 중복 될 수 없다.
 - 몰에 진행중인 주문이 있는 경우 제거(폐업) 할 수 없다.
 ```
 ```
 쇼핑몰회원(member)
 - (★)각 회원은 하나의 쇼핑몰에만 포함 될 수 있다.
 - 회원의 아이디와 이메일은 중복을 허용하지 않는다.
 - 회원은 활성(정상)/휴면/탈퇴의 상태를 가질 수 있다.
 ```

### 2. 도메인 설계
``운영자(Admin)``
- 운영자 고유ID(pk)
- 몰 정보(양방향 관계)
- 운영자 아이디 (중복검사 및 제한문자 vaildation 적용)
- 운영자명
- 연락처 (vaildation 적용)
- 이메일 (vaildation 적용)
- 생성일 (LocalDateTime)
- 마지막 수정일 (LocalDateTime)

``몰(Mall) -> TODO``
- 상점고유ID (pk)
- 운영자 고유ID (fk)
- 상점이름
- 상점 주도메인
- 지원국가코드
- 주소정보 -> embeded로 통합 (각 property는 오버라이딩해서 사업장 주소로 변경)
- 개설일 (LocalDateTime)

``쇼핑몰회원(member) -> TODO``
- 회원고유ID (pk)
- 상점번호 (fk, 어느상점의 회원인가?)
- 회원아이디 (unique key, 중복검사 및 제한문자 vaildation 적용)
- 회원명
- 성별
- 생년월일
- 주소정보 -> embeded로 통합
- 회원상태 (ENUM)
- 회원가입일 (LocalDateTime)
- 회원정보 수정일 (LocalDateTime)

``상품 (product) -> TODO``
- 123

``상품카테고리 -> TODO``
- 123

``주문 -> TODO``
- 주문ID (pk)
- 회원아이디(fk)
- 배송아이디(fk)
- orderProducts (일대다)
- 배송일

``배송정보 -> TODO``
- 배송정보ID (pk)
- 배송상태 (enum)
- 주소정보 -> embeded로 통합
- 주문정보 -> (일대일, order)

### 3. 도메인 개발
- DEFAULT DDL 작성
- Seed Data 작성
- repository, service layer 작성
- 도메인별 테스트 케이스 작성
- 컨트롤러

### 4. api 설계
``운영자(Admin) - Done``
```
GET /운영자
 - pagenation 적용 필요 (default 값 필요)

GET /운영자/운영자ID
 - 개인정보인 경우에는 암호화하여 확인 할 수 있도록 혹은 조회에서 제외

PUT /운영자/운영자ID
 - 휴대전화 번호, 주소를 수정 할 수 있다.

POST /운영자
 - 아이디 및 이메일 기반 중복검사

DELETE /운영자/운영자NO
 - 운영자ID 기반으로 변경(?)

```

``몰(Mall) - DOING``
```
GET /몰
GET /몰/{no}
GET /몰/운영자/{ID}
POST /몰
- 201
- 운영자가 없는 경우: 409 에러?

PUT /몰/{no}
- mall명, 주소정보 

DELETE /몰/{no}
```

### 5. Todo/Doing List
- `완료` 수정 API를 실행 할 때, 객체타입을 지닌 엔티티 처리 방식 확인 필요
    - 엔티티는 `@DynamicUpdate`를 통해서 변경된 항목만 수정하도록하고, 복합 값 타입은 객체를 불변객체로 생성하고 수정 시, 객체 자체를 재생성하여 등록하도록 수정
- `진행중` admin api controller mockMvc 테스트 케이스 작성
- GET /api/admins -> pagenation 적용 필요
- 몰(Mall) api 설계 필요
