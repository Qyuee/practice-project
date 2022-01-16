package com.practice.project.exception.exhandler;

/**
 * 부정확한 파라미터 입력
 */
public class ApiBadRequestException extends RuntimeException {
    public ApiBadRequestException() {
        super();
    }
    public ApiBadRequestException(String message) {
        super(message);
    }
}
