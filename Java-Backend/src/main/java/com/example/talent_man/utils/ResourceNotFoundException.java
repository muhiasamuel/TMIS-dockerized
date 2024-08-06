package com.example.talent_man.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Sets HTTP status code to 404 (Not Found)
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String field;
    private Object fieldValue;

    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " not found!");
        this.resourceName = resourceName;
    }

    public ResourceNotFoundException(String resourceName, String field, Object fieldValue) {
        super(resourceName + " with " + field + "='" + fieldValue + "' not found!");
        this.resourceName = resourceName;
        this.field = field;
        this.fieldValue = fieldValue;
    }

    // Getters for resourceName, field, and fieldValue (omitted for brevity)
}
