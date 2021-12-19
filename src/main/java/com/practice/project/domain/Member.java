package com.practice.project.domain;

import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.common.Gender;
import com.practice.project.domain.statusinfo.MemberStatus;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "member")
@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "mbr_no", nullable = false, updatable = false)
    private Long no;

    @ManyToOne
    @JoinColumn(name = "mall_no", nullable = false, updatable = false)
    private Mall mall;

    @Column(name = "mbr_id", unique = true, nullable = false, updatable = false)
    private String id;

    @Column(name = "mbr_name", length = 10, nullable = false, updatable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "mbr_gender", length = 2)
    private Gender gender;

    @Column(name = "mbr_email", length = 50, unique = true, updatable = false, nullable = false)
    private String email;

    @Column(name = "mbr_phone_number", length = 14)
    private String phNumber;

    @Column(name = "mbr_birthdate")
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "mbr_status", length = 10, columnDefinition = "default ACTIVE")
    private MemberStatus status;

    // 수정 메소드(제한적인 setter)
    public void changePhNumber(String phNumber) {
        this.phNumber = Optional.ofNullable(phNumber).orElse(this.phNumber);
    }

    public void changeBirthDate(LocalDate birthdate) {
        this.birthdate = Optional.ofNullable(birthdate).orElse(this.birthdate);
    }
}
