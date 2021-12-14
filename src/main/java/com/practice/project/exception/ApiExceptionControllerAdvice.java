package com.practice.project.exception;

import com.practice.project.exception.exhandler.ApiBadRequestException;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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
    @ExceptionHandler({MethodArgumentNotValidException.class, ApiBadRequestException.class})
    public ResponseEntity<ErrorResponse> ApiBadRequestExHandler(MethodArgumentNotValidException e) {
        return ResponseEntity.status(ErrorCase.BAD_REQUEST.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.BAD_REQUEST, e.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> ConstraintViolationExHandler(ConstraintViolationException e) {
        return ResponseEntity.status(ErrorCase.BAD_REQUEST.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.BAD_REQUEST, test(e.getConstraintViolations().iterator())));
    }

    protected String test(Iterator<ConstraintViolation<?>> iterator) {
        while (iterator.hasNext()) {
            ConstraintViolation<?> constraintViolation = iterator.next();
            log.error("1) {}", constraintViolation.getPropertyPath().toString());
            log.error("2) {}", constraintViolation.getInvalidValue());  // Page request [number: 0, size 1, sort: hello: ASC]
            log.error("3) {}", constraintViolation.getMessage());   // You should insert valid sort key.
            log.error("4) {}", Arrays.stream(constraintViolation.getExecutableParameters()).collect(Collectors.toList()));
        }

        return "test";
    }
}
