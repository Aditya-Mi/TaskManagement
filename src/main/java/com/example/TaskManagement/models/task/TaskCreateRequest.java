package com.example.TaskManagement.models.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCreateRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title must be less than or equal to 100 characters")
    private String title;

    @Size(max = 500, message = "Description must be less than or equal to 500 characters")
    private String description;

    @NotNull(message = "Priority is mandatory")
    @Positive(message = "Priority must be a positive number")
    private Integer priority;

    private boolean done;

    private LocalDate dueDate;
}
