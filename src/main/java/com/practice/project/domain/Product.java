package com.practice.project.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;
    private String name;
    private float price;

    // 상품 재고
    private int stock;

    // 상품 카테고리 - 하나의 상품은 여러개의 카테고리를 가질 수 있음
    @OneToMany(mappedBy = "product")
    private List<ProductCategory> productCategories = new ArrayList<>();
}
