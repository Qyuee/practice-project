package com.practice.project.domain;

import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.common.Country;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
}
