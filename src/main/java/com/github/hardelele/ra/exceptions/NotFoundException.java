package com.github.hardelele.ra.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {

    private HttpStatus code;
    private String message;

    public NotFoundException(String message, HttpStatus code) {
        this.code = code;
        this.message = message;
    }
}
