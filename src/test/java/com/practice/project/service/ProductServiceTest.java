package com.practice.project.service;

import com.practice.project.dto.ProductDto;
import com.practice.project.dto.ProductDto.ProductSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest
@ActiveProfiles("mysql")
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("mybatis - 상품 정보 저장")
    void save() {
        String name = "테스트 상품";
        float price = 1000;

        productService.save(name, price);
    }

    @Test
    @DisplayName("mybatis - 상품목록조회")
    void findAll() {
        List<ProductSearchDto> all = productService.findAll();
        for (ProductSearchDto productSearchDto : all) {
            log.info(" >> : {}", productSearchDto.toString());
        }
    }
}