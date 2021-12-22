package com.practice.project.domain.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Optional;

/**
 * 값 타입은 불변객체로 생성해서 참조공유를 막아야한다.
 * - setter 제거
 * - 가급적 flat하게 사용해야 한다.
 * - 값 타입이 복잡해지면 차라리 일대다 관계를 적용하는걸 고려해볼것
 * - 동일성 비교 (==)
 * - 엔티티가 아니므로 추적이 불가능하다.
 */
@Embeddable
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Address {
    @Enumerated(EnumType.STRING)
    private Country country;
    private String city;
    private String street;
    private String zipcode;
    private String detailAddress;

    public Address upsert(Address newAddress, Address beforeAddress) {
        return Address.builder()
                .country(Optional.ofNullable(newAddress.getCountry()).orElse(beforeAddress.getCountry()))
                .city(Optional.ofNullable(newAddress.getCity()).orElse(beforeAddress.getCity()))
                .street(Optional.ofNullable(newAddress.getStreet()).orElse(beforeAddress.getStreet()))
                .zipcode(Optional.ofNullable(newAddress.getZipcode()).orElse(beforeAddress.getZipcode()))
                .detailAddress(Optional.ofNullable(newAddress.getDetailAddress()).orElse(beforeAddress.getDetailAddress()))
                .build();
    }
}
