### 이슈내역

#### TEST
`테스트 코드에서 @BeforeAll, @AfterAll을 사용하는 경우`
```
@BeforeAll, @AfterAll 어노테이션은 테스트 시작 전/후에 설정 할 공통적인 설정을 정의 할 수 있음
기본적으로는 static method에 적용되어야 한다.

에러내용
- @BeforeAll method 'void com.practice.project.service.MallServiceTest.테스트_데이터_설정()' must be static unless the test class is annotated with @TestInstance(Lifecycle.PER_CLASS).

해결방안
- @TestInstance(Lifecycle.PER_CLASS)을 테스트 class에 설정하여 static method가 아닌 경우에도 사용 할 수 있도록 수정
- @TestInstance(Lifecycle.PER_CLASS): 클래스단위의 생명주기를 가진다.

테스트 데이터를 별도로 구성하여 데이터를 재활용하는게 더 나을 듯
(테스트 DB는 In-memory로 구성하자. 개발/운영환경과는 분리)
```