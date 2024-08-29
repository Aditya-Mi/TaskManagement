package com.example.TaskManagement.services;

import com.example.TaskManagement.UserNotFoundException;
import com.example.TaskManagement.exception.ResourceNotFoundException;
import com.example.TaskManagement.models.task.Task;
import com.example.TaskManagement.models.auth.User;
import com.example.TaskManagement.models.task.TaskCreateRequest;
import com.example.TaskManagement.models.task.TaskUpdateRequest;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public List<Task> getAllTasks(HttpServletRequest request) throws UserNotFoundException {
        String token = jwtService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(token);
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        return taskRepository.findByUserId(user.getId());
    }

    public Task getTaskById(int taskId,HttpServletRequest request) throws ResourceNotFoundException, UserNotFoundException {
        String token = jwtService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(token);
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if the task exists and belongs to the authenticated user
        return taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
    }

    public Task saveTask(
            TaskCreateRequest taskRequest,
            HttpServletRequest request
    ) throws UserNotFoundException {
        String token = jwtService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(token);
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setPriority(taskRequest.getPriority());
        task.setDone(taskRequest.isDone());
        task.setDueDate(taskRequest.getDueDate());
        task.setUserId(user.getId());
        Task taskSaved = taskRepository.save(task);
        log.info("Task saved: {}", taskSaved);
        return taskSaved;
    }

    public Task updateTask(TaskUpdateRequest taskRequest, HttpServletRequest request) throws UserNotFoundException, ResourceNotFoundException {
        String token = jwtService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(token);
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if the task exists and belongs to the authenticated user
        Task existingTask = taskRepository.findByIdAndUserId(taskRequest.getId(), user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskRequest.getId()));
        if (taskRequest.getTitle() != null) existingTask.setTitle(taskRequest.getTitle());
        if (taskRequest.getDescription() != null) existingTask.setDescription(taskRequest.getDescription());
        if (taskRequest.getPriority() != null) existingTask.setPriority(taskRequest.getPriority());
        if (taskRequest.getDone() != null) existingTask.setDone(taskRequest.getDone());
        if (taskRequest.getDueDate() != null) existingTask.setDueDate(taskRequest.getDueDate());
        Task taskSaved = taskRepository.save(existingTask);
        log.info("Task updated: {}", taskSaved);
        return taskSaved;
    }

    public void deleteTask(int id,HttpServletRequest request) throws UserNotFoundException,ResourceNotFoundException {
        String token = jwtService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(token);
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if the task exists and belongs to the authenticated user
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
        log.info("Task deleted with id: {}", id);
    }


}
