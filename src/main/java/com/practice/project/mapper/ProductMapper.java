package com.practice.project.mapper;

import com.practice.project.dto.ProductDto.ProductSearchDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("select * FROM product")
    List<ProductSearchDto> findAll();

    @Insert("INSERT INTO product (name, price) values (#{name}, #{price})")
    void save(@Param("name") String name, @Param("price") float price);
}
