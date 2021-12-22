package com.practice.project.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final int status;
    private final String code;
    private final String msg;

    public ErrorResponse(ErrorCase errorCase) {
        this.status = errorCase.getHttpStatus().value();
        this.code = errorCase.name();
        this.msg = errorCase.getMsg();
    }

    public ErrorResponse(ErrorCase errorCase, String msg) {
        this.status = errorCase.getHttpStatus().value();
        this.code = errorCase.name();
        this.msg = Optional.ofNullable(msg).orElse(errorCase.getMsg());
    }

    public ErrorResponse(ErrorCase errorCase, String code, String msg) {
        this.status = errorCase.getHttpStatus().value();
        this.code = Optional.ofNullable(code).orElse(errorCase.name());
        this.msg = Optional.ofNullable(msg).orElse(errorCase.getMsg());
    }
}
