package com.parkging.blog.apiapp.domain.member.exception.advice;

import com.parkging.blog.apiapp.domain.member.exception.UnavailableSignUpException;
import com.parkging.blog.apiapp.global.exception.ErrorMessageUtil;
import com.parkging.blog.apiapp.global.exception.ErrorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "com.parkging.blog.apiapp.domain.member")
@RequiredArgsConstructor
@Slf4j
public class MemberExceptionAdvice {

    private final ErrorMessageUtil eu;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult illegalArgumentException(IllegalArgumentException e) {
        log.error("[MemberExceptionAdvice.illegalArgumentException]", e);
        return eu.getErrorResult(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noResultException(NoResultException e) {
        log.error("[MemberExceptionAdvice.noResultException]", e);
        return eu.getErrorResult(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult noSuchElementException(NoSuchElementException e) {
        log.error("[MemberExceptionAdvice.NoSuchElementException]", e);
        return eu.getErrorResult(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult unavailableSignUpException(UnavailableSignUpException e) {
        log.error("[MemberExceptionAdvice.UnavailableSignUpException]", e);
        return eu.getErrorResult(e);
    }

//    //미사용; ExceptionHandlerFilter 에서 처리
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler
//    public ErrorResult loginFailException(LoginFailException e) {
//        log.error("[MemberExceptionAdvice.noResultException]", e);
//        String redirectUrl = "login/login-form";
//        return eu.getErrorResult(e, redirectUrl);
//    }

}
