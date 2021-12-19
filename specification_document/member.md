### 회원(member)

---
#### 1. 비즈니스 사양
- (★)각 회원은 하나의 쇼핑몰에만 포함 될 수 있다. (= 각 쇼핑몰 별로 회원이 상이하다.)
- 회원의 아이디와 이메일은 중복을 허용하지 않는다.
- 회원은 활성(정상)/휴면/탈퇴의 상태를 가질 수 있다.
- 회원의 진행중인 주문이 있는 경우 상태변경은 할 수 없다.
- 회원은 N개의 배송을 위한 주소목록을 가질 수 있다.
---
#### 2. 도메인 설계
```sql
-- 회원(Member)

```
---
#### 3. API 설계
```
특정 몰 회원 목록 조회

GET /api/mall/{no}/members
- Content-type: application/json;charset=UTF-8
- 필수값: 몰 고유번호
- 파라미터: page, size, sort
- 제한사항
    - sort 기준값: '가입일, '생년월일순'
- 응답 항목
    - count: 검색 결과수
    - data: 회원no, 회원id, 회원명, 회원이메일, 성별, 휴대번호, 생년월일, 회원상태
```

```
특정 몰 회원 수 조회

GET /api/mall/{no}/members/count
- Content-type: application/json;charset=UTF-8
- 필수값: 몰 고유번호
- 파라미터: 성별(gender), 회원상태(status)
```

```
특정 몰의 특정 회원 정보 조회

GET /api/mall/{no}/members/{id}
- Content-type: application/json;charset=UTF-8
- 필수값: 몰 고유번호, 회원ID
- 응답값: 회원No, 회원ID, 이름, 이메일, 휴대번호, 성별, 생년월일, 회원상태

```

```
회원 등록

POST /api/mall/{no}/members
- 필수값: 몰 고유번호
- Body: 회원ID, 이름, 이메일, 휴대번호, 성별, 생년월일
- 회원 상태는 기본적으로 'ACTIVE' 상태
```

```
회원 수정

PUT /api/mall/{no}/members/{id}
- 필수값: 몰 고유번호, 회원ID
- Body: 휴대번호, etc..
```

```
회원 상태변경

PUT /api/mall/{no}/members/{id}/status/change
- 필수값: 몰 고유번호, 회원ID
- 상태값: 휴면처리, 휴면해제, 정지, 블랙리스트 설정/해제
```

```
회원 삭제

DELETE /api/mall/{no}/members/{id}
- 필수값: 몰 고유번호, 회원ID
- 제한사항
    - 진행중인 주문이 있는 회원의 경우 탈퇴 불가
```

---
#### DTO 설계
1. GET 조회 요청
   - 몰 고유번호, 회원ID
2. POST 회원등록 요청
   - 회원ID, 이름, 이메일, 휴대번호, 성별, 생년월일
3. POST 회원등록 응답
   - 몰NO, 회원ID, 이름, 이메일, 휴대번호, 성별, 생년월일

