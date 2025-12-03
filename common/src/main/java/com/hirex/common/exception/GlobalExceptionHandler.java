package com.hirex.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyAppliedException.class)
    public ResponseEntity<ApiError> handleAlreadyApplied(AlreadyAppliedException ex) {
        log.warn("Duplicate job application attempt: {}", ex.getMessage());
        ApiError body = new ApiError(ex.getMessage(), HttpStatus.CONFLICT.value(), Instant.now());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        ApiError body = new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND.value(), Instant.now());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VacancyNotFoundException.class)
    public ResponseEntity<ApiError> handleVacancyNotFound(VacancyNotFoundException ex) {
        log.warn("Vacancy not found: {}", ex.getMessage());
        ApiError body = new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND.value(), Instant.now());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}