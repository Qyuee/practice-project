package com.practice.project.domain;

import javax.persistence.*;

@Entity
@Table(name = "DELIVERY")
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "DELIVERY_ID")
    private Long id;
}
