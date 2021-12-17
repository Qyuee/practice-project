package com.practice.project.domain;

import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.common.Country;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "mall")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mall extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "mall_no")
    private Long no;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin_no", nullable = false, updatable = false)
    private Admin admin;

    @OneToMany(mappedBy = "mall")
    private List<Member> memberList = new ArrayList<>();

    @Column(name = "mall_name", length = 50, nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "country_type", nullable = false, updatable = false)
    private Country countryType;

    @Embedded
    private Address address;
}
