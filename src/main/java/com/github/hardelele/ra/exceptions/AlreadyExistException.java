package com.github.hardelele.ra.exceptions;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends RuntimeException {

    private HttpStatus code;
    private String message;

    public AlreadyExistException(String message, HttpStatus code) {
        this.code = code;
        this.message = message;
    }
}
