package com.practice.project.exception.exhandler;

public class ApiResourceDuplicateException extends RuntimeException {
    public ApiResourceDuplicateException() {
        super();
    }

    public ApiResourceDuplicateException(String message) {
        super(message);
    }
}
