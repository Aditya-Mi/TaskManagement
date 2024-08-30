package com.example.TaskManagement.exception;

public class TaskOperationException extends RuntimeException {
    public TaskOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
