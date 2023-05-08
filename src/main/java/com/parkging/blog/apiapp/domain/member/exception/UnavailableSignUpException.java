package com.parkging.blog.apiapp.domain.member.exception;

public class UnavailableSignUpException extends RuntimeException {
    public UnavailableSignUpException() {
        super();
    }

    public UnavailableSignUpException(String message) {
        super(message);
    }

    public UnavailableSignUpException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnavailableSignUpException(Throwable cause) {
        super(cause);
    }

    protected UnavailableSignUpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
