package com.parkging.blog.apiapp.domain.post.exception;

public class ChildCategoryExistException extends RuntimeException {
    public ChildCategoryExistException() {
        super();
    }

    public ChildCategoryExistException(String message) {
        super(message);
    }

    public ChildCategoryExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChildCategoryExistException(Throwable cause) {
        super(cause);
    }

    protected ChildCategoryExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
