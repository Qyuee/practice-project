package com.practice.project.exception.exhandler;

public class ApiBadRequestException extends RuntimeException {
    public ApiBadRequestException() {
        super();
    }

    public ApiBadRequestException(String message) {
        super(message);
    }
}
