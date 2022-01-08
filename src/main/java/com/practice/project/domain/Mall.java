package com.practice.project.domain;

import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.common.Country;
import com.practice.project.domain.statusinfo.MallStatus;
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
    @JoinColumn(name = "admin_no", nullable = false, updatable = false)
    private Admin admin;

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "country_type", length = 2, nullable = false, updatable = false)
    private Country countryType;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, columnDefinition = "varchar(10) default 'NORMAL'")
    private MallStatus mallStatus;

    @OneToMany(mappedBy = "mall")
    private List<Member> memberList = new ArrayList<>();


    /* 수정 메소드 (setter는 제한적으로) */
    public void changeMallName(String newMallName) {
        this.name = Optional.ofNullable(newMallName).orElse(this.name);
    }
    public void changeAddress(Address newAddress) {
        this.address = this.address.upsert(newAddress);
    }
    public void changeStatus(MallStatus newStatus) {
        this.mallStatus = Optional.ofNullable(newStatus).orElse(this.mallStatus);
    }
}
