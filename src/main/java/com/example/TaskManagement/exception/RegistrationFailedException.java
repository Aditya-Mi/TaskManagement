package com.example.TaskManagement.exception;

public class RegistrationFailedException extends RuntimeException {
    public RegistrationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
