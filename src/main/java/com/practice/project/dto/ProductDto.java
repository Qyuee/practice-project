package com.practice.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

/**
 * Product Dto
 */
@NoArgsConstructor(access = PROTECTED)
public class ProductDto {

    @Data
    public static class ProductSearchDto {
        private Long no;
        private String name;
    }
}
