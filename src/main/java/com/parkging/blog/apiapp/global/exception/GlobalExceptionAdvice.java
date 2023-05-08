package com.parkging.blog.apiapp.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "com.parkging.blog.apiapp")
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionAdvice {

    private final ErrorMessageUtil eu;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noSuchElementException(NoSuchElementException e) {
        log.error("[GlobalExceptionAdvice.NoSuchElementException]", e);
        return eu.getErrorResult(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noResultException(NoResultException e) {
        log.error("[GlobalExceptionAdvice.noResultException]", e);
        return eu.getErrorResult(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noSuchMessageException(NoSuchMessageException e) {
        log.error("[GlobalExceptionAdvice.noSuchMessageException]", e);
        return eu.getErrorResult(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("[GlobalExceptionAdvice.JsonParseException]", e);
        return eu.getErrorResult(e);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResult accessDeniedException(org.springframework.security.access.AccessDeniedException e) {
        log.error("[GlobalExceptionAdvice.AccessDeniedException]", e);
        return eu.getErrorResult("error.forbidden", null, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult bindException(BindException e) {
        log.error("[GlobalExceptionAdvice.bindException]", e);

        String fieldErrorCode = "error.field.invalid";
        String errorMessage = e.getFieldError().getField() + " 은(는) " + e.getFieldError().getDefaultMessage();

        return eu.getErrorResult(fieldErrorCode, new String[]{errorMessage}, null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult runtimeException(RuntimeException e) {
        log.error("[GlobalExceptionAdvice.runtimeException]", e);
        return eu.getErrorResult(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exception(Exception e) {
        log.error("[GlobalExceptionAdvice.exception]", e);
        return eu.getErrorResult(e);
    }

}
