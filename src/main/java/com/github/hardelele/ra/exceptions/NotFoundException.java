package com.github.hardelele.ra.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class NotFoundException extends RuntimeException {

    private String NOT_FOUND = "Not found ";

    private HttpStatus httpStatus;
    private String message;

    public NotFoundException(String message, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.message = NOT_FOUND + message;
    }
}
