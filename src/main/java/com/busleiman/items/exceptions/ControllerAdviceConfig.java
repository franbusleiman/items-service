package com.busleiman.items.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerAdviceConfig {

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 409
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleException(NotFoundException notFoundException) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
    }
}
