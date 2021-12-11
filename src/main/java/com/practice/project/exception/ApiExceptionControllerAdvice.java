package com.practice.project.exception;

import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionControllerAdvice {

    /**
     * ApiResourceDuplicateException 예외를 반환하는 경우 409에러를 응답
     */
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(ApiResourceConflictException.class)
    public ResponseEntity<ErrorResponse> ApiResourceDuplicateExHandler(ApiResourceConflictException e) {
        return ResponseEntity.status(ErrorCase.CONFLICT.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler({ApiResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> ApiResourceNotFoundExHandler(ApiResourceNotFoundException e) {
        return ResponseEntity.status(ErrorCase.NOT_FOUND.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.NOT_FOUND, e.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return ResponseEntity.status(ErrorCase.BAD_REQUEST.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.BAD_REQUEST, e.getMessage()));
    }
}
