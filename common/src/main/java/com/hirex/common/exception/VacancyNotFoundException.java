package com.hirex.common.exception;

public class VacancyNotFoundException extends RuntimeException {
    public VacancyNotFoundException(String message) {
        super(message);
    }
}