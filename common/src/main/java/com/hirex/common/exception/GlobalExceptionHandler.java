package com.hirex.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AlreadyAppliedException.class, UserNotFoundException.class, VacancyNotFoundException.class})
    public ResponseEntity<ApiError> handleAlreadyApplied(AlreadyAppliedException ex) {
        ApiError body = new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), Instant.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}