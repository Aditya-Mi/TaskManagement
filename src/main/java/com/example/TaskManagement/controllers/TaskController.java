package com.example.TaskManagement.controllers;

import com.example.TaskManagement.UserNotFoundException;
import com.example.TaskManagement.models.Task;
import com.example.TaskManagement.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks(HttpServletRequest request) throws UserNotFoundException {
        return ResponseEntity.ok().body(taskService.getAllTasks(request));
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task, HttpServletRequest request) throws UserNotFoundException {
        log.info("Creating new task: {}", task);
        return ResponseEntity.ok().body(taskService.saveTask(task, request));
    }

    @PutMapping("/tasks")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        return ResponseEntity.ok().body(taskService.updateTask(task));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().body("Deleted Task");
    }
}
