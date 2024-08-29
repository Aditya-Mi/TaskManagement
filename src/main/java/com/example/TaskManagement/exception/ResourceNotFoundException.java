package com.example.TaskManagement.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

