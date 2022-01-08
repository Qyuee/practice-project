package com.practice.project.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "member_address")
@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAddress {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_address_no")
    private Long no;
}
