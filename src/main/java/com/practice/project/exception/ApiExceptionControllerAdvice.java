package com.practice.project.exception;

import com.practice.project.exception.exhandler.ApiBadRequestException;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

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

    /**
     * 404 에러 응답 정의
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({ApiResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> ApiResourceNotFoundExHandler(ApiResourceNotFoundException e) {
        return ResponseEntity.status(ErrorCase.NOT_FOUND.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.NOT_FOUND, e.getMessage()));
    }

    /**
     * 400 에러 응답 정의
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ApiBadRequestException.class})
    public ResponseEntity<ErrorResponse> ApiBadRequestExHandler(ApiBadRequestException e) {
        return ResponseEntity.status(ErrorCase.BAD_REQUEST.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.BAD_REQUEST, e.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> MethodArgumentExHandler(MethodArgumentNotValidException e) {
        return ResponseEntity.status(ErrorCase.BAD_REQUEST.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.BAD_REQUEST, e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> MethodArgumentTypeMissExHandler(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(ErrorCase.BAD_REQUEST.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.BAD_REQUEST, e.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> ConstraintViolationExHandler(ConstraintViolationException e) {
        return ResponseEntity.status(ErrorCase.BAD_REQUEST.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.BAD_REQUEST, e.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> DataIntegrityViolationExHandler(DataIntegrityViolationException e) {
        return ResponseEntity.status(ErrorCase.BAD_REQUEST.getHttpStatus().value())
                .body(new ErrorResponse(ErrorCase.BAD_REQUEST, e.getMessage()));
    }
}
