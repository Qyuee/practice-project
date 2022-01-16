package com.practice.project.exception.exhandler;

public class ApiResourceConflictException extends RuntimeException {
    public ApiResourceConflictException(String message) {
        super(message);
    }
}
