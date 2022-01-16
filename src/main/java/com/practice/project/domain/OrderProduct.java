package com.practice.project.domain;

import javax.persistence.*;

@Entity
@Table(name = "ORDER_PRODUCT")
public class OrderProduct {
    @Id
    @GeneratedValue
    @Column(name = "ORDER_PRODUCT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
}
