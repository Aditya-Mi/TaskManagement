package com.example.TaskManagement.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ToString
public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final List<String> errors;
    private final LocalDateTime timestamp;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
        this.timestamp = LocalDateTime.now();
    }
}
