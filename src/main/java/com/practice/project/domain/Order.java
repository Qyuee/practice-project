package com.practice.project.domain;

import com.practice.project.domain.common.BaseTime;
import com.practice.project.domain.statusinfo.OrderStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "order_no")
    private Long no;

    // 배송상태는 1:1
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_no")
    private Delivery delivery;

    // 여러갸의 주문은 하나의 회원에 속한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    // @TODO cascade 타입에 대해서 정리필요
    // 주문상품 정보
    // 하나의 주문은 여러개의 주문상품을 가질 수 있다.
    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
