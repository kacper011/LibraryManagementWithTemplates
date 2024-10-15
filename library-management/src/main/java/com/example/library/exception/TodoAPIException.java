package com.example.library.exception;

import org.springframework.http.HttpStatus;

public class TodoAPIException extends RuntimeException {
    private final HttpStatus status;

    public TodoAPIException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
