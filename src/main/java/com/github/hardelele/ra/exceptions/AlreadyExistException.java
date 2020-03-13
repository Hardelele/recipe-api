package com.github.hardelele.ra.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class AlreadyExistException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;

    public AlreadyExistException(String message, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
