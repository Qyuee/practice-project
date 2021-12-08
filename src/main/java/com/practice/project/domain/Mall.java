package com.practice.project.domain;

import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.common.Country;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "mall")
@Getter
@Setter
public class Mall extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "mall_no")
    private Long no;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin_no")
    private Admin admin;

    @Column(name = "mall_name", length = 50, nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "country_type", nullable = false, updatable = false)
    private Country countryType;

    @Embedded
    private Address address;
}
