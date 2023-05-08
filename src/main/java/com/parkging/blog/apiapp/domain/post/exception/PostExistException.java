package com.parkging.blog.apiapp.domain.post.exception;

public class PostExistException extends RuntimeException{
    public PostExistException() {
        super();
    }

    public PostExistException(String message) {
        super(message);
    }

    public PostExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostExistException(Throwable cause) {
        super(cause);
    }

    protected PostExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
