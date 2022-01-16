package com.practice.project.domain;

import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.common.Gender;
import com.practice.project.domain.statusinfo.MemberStatus;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "MEMBER")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@DynamicInsert
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "MEMBER_NO", nullable = false, updatable = false)
    private Long no;

    @ManyToOne
    @JoinColumn(name = "MALL_NO", nullable = false, updatable = false)
    private Mall mall;

    @Column(name = "MEMBER_ID", unique = true, nullable = false, updatable = false)
    private String memberId;

    @Column(name = "NAME", length = 10, nullable = false, updatable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", length = 2)
    private Gender gender;

    @Column(name = "EMAIL", length = 50, unique = true, updatable = false, nullable = false)
    private String email;

    @Column(name = "PHONE_NUMBER", length = 14)
    private String phNumber;

    @Column(name = "BIRTHDATE")
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 10, columnDefinition = "varchar(10) default 'ACTIVE'")
    private MemberStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ADDR_ID")
    private MemberAddress memberAddress;

    // 수정 메소드(제한적인 setter)
    public void changePhNumber(String phNumber) {
        this.phNumber = Optional.ofNullable(phNumber).orElse(this.phNumber);
    }

    public void changeBirthDate(LocalDate birthdate) {
        this.birthdate = Optional.ofNullable(birthdate).orElse(this.birthdate);
    }
}
