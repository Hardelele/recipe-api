package com.github.hardelele.ra.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class NotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message = "Not found ";

    public NotFoundException(String message, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.message = this.message + message;
    }
}
