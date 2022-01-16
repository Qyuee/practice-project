package com.practice.project.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "MEMBER_ADDR")
@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAddress {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_ADDR_ID")
    private Long id;
}
