package com.practice.project.domain;

import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.BaseTime;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "admin")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
public class Admin extends BaseTime {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "adm_no", nullable = false, updatable = false)
    private Long no;

    // 운영자:상점 -> 1:N -> 연관관계 주인 => 몰
    @OneToMany(mappedBy = "admin")
    private List<Mall> mallList = new ArrayList<>();

    @Column(name = "adm_id", unique = true, updatable = false, nullable = false)
    private String id;

    @Column(name = "adm_email", length = 50, unique = true, updatable = false, nullable = false)
    private String email;

    @Column(name = "adm_name", updatable = false, nullable = false)
    private String name;

    @Column(name = "adm_phone_number")
    private String phNumber;

    @Embedded
    private Address address;

    public void changePhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public void changeAddress(Address address) {
        this.address = Address.builder()
                .country(Optional.ofNullable(address.getCountry()).orElse(this.getAddress().getCountry()))
                .city(Optional.ofNullable(address.getCity()).orElse(this.getAddress().getCity()))
                .street(Optional.ofNullable(address.getStreet()).orElse(this.getAddress().getStreet()))
                .zipcode(Optional.ofNullable(address.getZipcode()).orElse(this.getAddress().getZipcode()))
                .detailAddress(Optional.ofNullable(address.getDetailAddress()).orElse(this.getAddress().getDetailAddress()))
                .build();
    }
}
