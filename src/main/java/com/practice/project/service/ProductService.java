package com.practice.project.service;

import com.practice.project.dto.ProductDto.ProductSearchDto;
import com.practice.project.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<ProductSearchDto> findAll() {
        return productMapper.findAll();
    }

    public void save(String name, float price) {
        productMapper.save(name, price);
    }
}
