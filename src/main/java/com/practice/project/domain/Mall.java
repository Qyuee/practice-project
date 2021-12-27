package com.practice.project.domain;

import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.common.Country;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "mall")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mall extends BaseTime {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "mall_no", nullable = false, updatable = false)
    private Long no;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "adm_no", nullable = false, updatable = false)
    private Admin admin;

    @Column(name = "mall_name", length = 50, nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "mall_country_type", length = 2, nullable = false, updatable = false)
    private Country countryType;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "mall")
    private List<Member> memberList = new ArrayList<>();


    /* 수정 메소드 (setter는 제한적으로) */
    public void changeMallName(String newMallName) {
        this.name = Optional.ofNullable(newMallName).orElse(this.name);
    }

    public void changeAddress(Address newAddress) {
        this.address = this.address.upsert(newAddress);
    }
}
