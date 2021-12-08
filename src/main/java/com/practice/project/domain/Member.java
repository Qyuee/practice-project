package com.practice.project.domain;

import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.statusinfo.MemberStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "member")
@Getter
@Setter
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_no", nullable = false, updatable = false)
    private Long no;

    @OneToOne
    @JoinColumn(name = "mall_no")
    private Mall mall;

    @Column(name = "member_id", nullable = false, updatable = false, unique = true)
    private String id;

    @Column(name = "member_name", length = 10, nullable = false, updatable = false)
    private String name;

    @Column(length = 2)
    private String gender;

    @Column(length = 100)
    private String email;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private MemberStatus status;
}
