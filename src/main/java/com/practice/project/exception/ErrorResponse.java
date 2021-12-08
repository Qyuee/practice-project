package com.practice.project.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String msg;

    public ErrorResponse(ErrorCase errorCase) {
        this.status = errorCase.getHttpStatus().value();
        this.error = errorCase.getHttpStatus().name();
        this.code = errorCase.name();
        this.msg = errorCase.getMsg();
    }

    public ErrorResponse(ErrorCase errorCase, String msg) {
        this.status = errorCase.getHttpStatus().value();
        this.error = errorCase.getHttpStatus().name();
        this.code = errorCase.name();
        this.msg = Optional.ofNullable(msg).orElse(errorCase.getMsg());
    }

    public ErrorResponse(ErrorCase errorCase, String code, String msg) {
        this.status = errorCase.getHttpStatus().value();
        this.error = errorCase.getHttpStatus().name();
        this.code = Optional.ofNullable(code).orElse(errorCase.name());
        this.msg = Optional.ofNullable(msg).orElse(errorCase.getMsg());
    }
}
