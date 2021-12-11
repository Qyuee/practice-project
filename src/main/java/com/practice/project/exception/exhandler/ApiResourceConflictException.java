package com.practice.project.exception.exhandler;

public class ApiResourceConflictException extends RuntimeException {
    public ApiResourceConflictException() {
        super();
    }

    public ApiResourceConflictException(String message) {
        super(message);
    }
}
