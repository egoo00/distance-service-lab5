package com.example.distanceservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final HttpStatus BAD_REQUEST_STATUS = HttpStatus.BAD_REQUEST;
    private static final HttpStatus INTERNAL_SERVER_ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred: ";

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<String> handleCityNotFoundException(CityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST_STATUS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(UNEXPECTED_ERROR_MESSAGE + ex.getMessage(), INTERNAL_SERVER_ERROR_STATUS);
    }
}
