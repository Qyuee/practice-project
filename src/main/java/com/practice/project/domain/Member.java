package com.practice.project.domain;

import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.statusinfo.MemberStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_no", nullable = false, updatable = false)
    private Long no;

    @ManyToOne
    @JoinColumn(name = "mall_no", nullable = false, updatable = false)
    private Mall mall;

    @Column(name = "member_id", nullable = false, updatable = false, unique = true)
    private String id;

    @Column(name = "member_name", length = 10, nullable = false, updatable = false)
    private String name;

    @Column(length = 2)
    private String gender;

    @Column(length = 50, unique = true, updatable = false, nullable = false)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private MemberStatus status;
}
