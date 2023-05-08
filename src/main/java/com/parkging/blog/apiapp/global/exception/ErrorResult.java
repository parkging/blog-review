package com.parkging.blog.apiapp.global.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResult {
    private String errorCode;
    private String message;
    private String redirectUrl;

    @Builder
    public ErrorResult(String errorCode, String message, String redirectUrl) {
        this.errorCode = errorCode;
        this.message = message;
        this.redirectUrl = redirectUrl;
    }
}
