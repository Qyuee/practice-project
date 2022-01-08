package com.practice.project.dto;

import lombok.Data;

/**
 * Product Dto
 */
public class ProductDto {

    @Data
    public static class ProductSearchDto {
        private Long no;
        private String name;
    }
}
