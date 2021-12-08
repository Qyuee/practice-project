package com.practice.project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCase {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "wrong request."),  // 400
    NOT_FOUND(HttpStatus.NOT_FOUND, "Entity not found."),   // 404
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Not allowed method."),   // 405
    CONFLICT(HttpStatus.CONFLICT, "Conflict"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");  // 500

    private final HttpStatus httpStatus;
    private final String msg;
}
