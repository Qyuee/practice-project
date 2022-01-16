package com.practice.project.domain;

import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.BaseTime;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "ADMIN_NO", nullable = false, updatable = false)
    private Long no;

    @Column(name = "ADMIN_ID", unique = true, updatable = false, nullable = false)
    private String adminId;

    @Column(name = "EMAIL", length = 50, unique = true, updatable = false, nullable = false)
    private String email;

    @Column(name = "NAME", updatable = false, nullable = false)
    private String name;

    @Column(name = "PHONE_NUMBER")
    private String phNumber;

    @Embedded
    private Address address;

    // 운영자:상점 -> 1:N -> 연관관계 주인 => 몰
    @OneToMany(mappedBy = "admin")
    private List<Mall> mallList = new ArrayList<>();

    public void changePhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public void changeAddress(Address address) {
        this.address = this.address.upsert(address);
    }
}
