package com.hirex.portal.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Object id) {
        super("Entity with ID " + id + " not found. It may have been deleted by another user.");
    }
}
