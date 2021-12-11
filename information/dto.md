```
참고문서
- https://tecoble.techcourse.co.kr/post/2021-04-25-dto-layer-scope/
- https://velog.io/@aidenshin/DTO%EC%97%90-%EA%B4%80%ED%95%9C-%EA%B3%A0%EC%B0%B0
- https://dbbymoon.tistory.com/4
```

### DTO란?
```
DTO(Data Transfer Object): 계층(View, Controller, Service, Repository)간의 데이터를 교환하기 위해서 사용하는 객체

사용하는 이유
- 보통의 경우 controller로부터 요청에 대한 응답으로 Entity정보를 곧바로 응답해주지 않도록 해야한다.
- 민감한 도메인 모델의 데이터가 외부에 노출될 수 있기에 필요한 부분만 노출되도록 해준다.
- 도메인 모델(Entity)가 변경되면 View에게 응답해주는 데이터 사양이 달라지는 상호간의 결합이 강한 상태가 발생한다.
- 불필요한 데이터를 가지고 각 계층을 이동하므로 불필요한 리소스를 사용 할 것이다.

view -> (dto) -> Controller -> (dto) -> Service -> (dto) -> repository -> (entity) -> DB
```
----

#### DTO 생성방법
- 각 View에서 원하는 도메인 모델의 데이터만을 별도로 캡슈화하여 선택적으로 데이터를 응답 할 수 있도록 한다.
- Domain 모델을 DTO로 변환 할 수 있는 생성자 혹은 별도의 메소드를 구성한다.
- view나 api의 용도에 따라서 dto를 별도로 구성하여 구분한다. (너무 dto가 많아져서 복잡해질 수도 있음에 주의)
 - 하나의 dto class내부에 inner class를 작성하여 각 특성에 맞도록 사용한다. (createDto, updateDto, searchDto, deleteDto etc..)

----

#### DTO형태로 요청을 받아도 결국 domain 모델(Entity)로 다시 변환을 해줘야한다. 어디서 해야할까?
1. Controller에서 한다?  
```요청: View -> controller -> (dto -> domain) -> Service -> Repository```  
```응답: Repository -> Service -> Controller -> (domain -> dto) -> View```
- View로부터 받은 DTO를 Controller에서 domain(Entity)로 변경하고 이를 Service로 전달한다.
- Service는 Controller에게 domain을 전달하고 Controller에서 이를 DTO로 변환하여 view에게 응답한다.

`문제점`
- View에 필요하지 않는 데이터가 Controller Layer까지 전달된다.
- Controller는 결국 여러 Service의 응답 domain 정보를 dto로 변환해야하므로 Service로직이 Controller에 포함된다.
- 여러 도메인 정보가 필요한 경우 여러 Service에 의존하게된다.

`결론`
- Controller에서 변환을 해줘도 된다. 근데 꼭 이렇게 해야하는건 아니다.

2. Service Layer에서 한다?
```요청: View -> controller -> Service -> (dto -> domain) -> Repository```  
```응답: Repository -> Service -> (domain -> dto) -> Controller -> View```
- Service Layer란?(마틴 파울러): 어플리케이션 경계를 정의하고 비즈니스 로직 등 도메인을 캡슐화하는 역할. 도메인을 보호한다.

`내용`
- Controller에서 요구하는 복잡한 처리 결과에 맞는 dto를 service에서 처리하여 반환해준다.
- Controller는 Service가 응답한 dto에 대해서 변환/복잡한 로직에 대해서 고민 할 필요가 없다.

`결론`
- Service Layer에서 dto를 변환해주는것이 더 자연스럽고 각 계층의 특성에 맞다고 보여진다.

3. Repository Layer에서는?
- Repository 영역은 Entitiy 영속성을 관장하는 영역이므로 지양한다.

  
