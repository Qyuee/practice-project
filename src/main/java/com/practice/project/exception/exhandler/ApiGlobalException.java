package com.practice.project.exception.exhandler;

import com.practice.project.exception.ErrorCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiGlobalException extends RuntimeException {
    private final ErrorCase errorCase;
}
