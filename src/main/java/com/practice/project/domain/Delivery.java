package com.practice.project.domain;

import javax.persistence.*;

@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_no")
    private Long no;
}
