package com.practice.project.domain;

import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.statusinfo.MallStatus;
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
    @Column(name = "admin_no", nullable = false, updatable = false)
    private Long no;

    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private String id;

    @Column(name = "email", length = 50, unique = true, updatable = false, nullable = false)
    private String email;

    @Column(name = "name", updatable = false, nullable = false)
    private String name;

    @Column(name = "phone_number")
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
