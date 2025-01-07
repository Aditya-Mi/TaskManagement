package com.example.TaskManagement.controllers;

import com.example.TaskManagement.UserNotFoundException;
import com.example.TaskManagement.exception.ResourceNotFoundException;
import com.example.TaskManagement.models.task.Task;
import com.example.TaskManagement.models.task.TaskCreateRequest;
import com.example.TaskManagement.models.task.TaskUpdateRequest;
import com.example.TaskManagement.services.JwtService;
import com.example.TaskManagement.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class TaskController {
    private final TaskService taskService;
    private final JwtService jwtService;

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks(
            HttpServletRequest request
    ) throws UserNotFoundException {
        String token = jwtService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok().body(taskService.getAllTasks(username));
    }

    @GetMapping("tasks/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable @Positive(message = "Task ID must be a positive number") int id
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(taskService.getTaskById(id));
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(
            @Valid @RequestBody TaskCreateRequest task, HttpServletRequest request
    ) throws UserNotFoundException {
        String token = jwtService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok().body(taskService.saveTask(task, username));
    }

    @PutMapping("/tasks")
    public ResponseEntity<Task> updateTask(
            @Valid @RequestBody TaskUpdateRequest task
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(taskService.updateTask(task));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable @NotNull @Positive(message = "Task ID must be a positive number") int id
    ) throws ResourceNotFoundException {
        taskService.deleteTask(id);
        return ResponseEntity.ok().body("Task deleted successfully.");
    }
}
