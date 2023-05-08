package com.parkging.blog.apiapp.global.exception;

public class RevalidationException extends RuntimeException {
    public RevalidationException() {
        super();
    }

    public RevalidationException(String message) {
        super(message);
    }

    public RevalidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RevalidationException(Throwable cause) {
        super(cause);
    }

    protected RevalidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
