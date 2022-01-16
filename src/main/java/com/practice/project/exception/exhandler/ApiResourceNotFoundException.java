package com.practice.project.exception.exhandler;

public class ApiResourceNotFoundException extends RuntimeException {
    public ApiResourceNotFoundException(String message) {
        super(message);
    }
}
