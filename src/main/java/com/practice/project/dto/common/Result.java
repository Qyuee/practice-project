package com.practice.project.dto.common;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Result<T> {
    private int count;
    private T data;

    public Result(T data) {
        this.data = data;
    }
}
