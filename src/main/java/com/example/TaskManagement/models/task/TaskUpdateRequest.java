package com.example.TaskManagement.models.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskUpdateRequest {

    @NotNull(message = "Task ID is mandatory")
    @Positive(message = "Task ID must be a positive number")
    private Integer id;

    private String title;
    private String description;
    private Integer priority;
    private Boolean done;  // Use Boolean to allow null for optional updates
    private LocalDate dueDate;
}

