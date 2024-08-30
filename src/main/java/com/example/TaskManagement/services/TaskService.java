package com.example.TaskManagement.services;

import com.example.TaskManagement.UserNotFoundException;
import com.example.TaskManagement.exception.DatabaseOperationException;
import com.example.TaskManagement.exception.ResourceNotFoundException;
import com.example.TaskManagement.exception.TaskOperationException;
import com.example.TaskManagement.models.task.Task;
import com.example.TaskManagement.models.auth.User;
import com.example.TaskManagement.models.task.TaskCreateRequest;
import com.example.TaskManagement.models.task.TaskUpdateRequest;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
        try {
            String token = jwtService.getTokenFromHeader(request);
            String username = jwtService.extractUsername(token);
            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            return taskRepository.findByUserId(user.getId());
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Error occurred while fetching tasks", e);
        } catch (UserNotFoundException e){
            throw e;
        } catch (Exception e) {
            throw new TaskOperationException("An unexpected error occurred while fetching tasks", e);
        }
    }

    public Task getTaskById(int taskId,HttpServletRequest request) throws ResourceNotFoundException, UserNotFoundException {
        try {
            String token = jwtService.getTokenFromHeader(request);
            String username = jwtService.extractUsername(token);
            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            return taskRepository.findByIdAndUserId(taskId, user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Error occurred while fetching task", e);
        } catch (ResourceNotFoundException | UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TaskOperationException("An unexpected error occurred while fetching task", e);
        }
    }

    public Task saveTask(
            TaskCreateRequest taskRequest,
            HttpServletRequest request
    ) throws UserNotFoundException {
        try {
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

            return taskRepository.save(task);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Error occurred while saving task", e);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TaskOperationException("An unexpected error occurred while saving task", e);
        }
    }

    public Task updateTask(TaskUpdateRequest taskRequest, HttpServletRequest request) throws UserNotFoundException, ResourceNotFoundException {
        try {
            String token = jwtService.getTokenFromHeader(request);
            String username = jwtService.extractUsername(token);
            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Task existingTask = taskRepository.findByIdAndUserId(taskRequest.getId(), user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskRequest.getId()));

            if (taskRequest.getTitle() != null) existingTask.setTitle(taskRequest.getTitle());
            if (taskRequest.getDescription() != null) existingTask.setDescription(taskRequest.getDescription());
            if (taskRequest.getPriority() != null) existingTask.setPriority(taskRequest.getPriority());
            if (taskRequest.getDone() != null) existingTask.setDone(taskRequest.getDone());
            if (taskRequest.getDueDate() != null) existingTask.setDueDate(taskRequest.getDueDate());

            return taskRepository.save(existingTask);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Error occurred while updating task", e);
        } catch (ResourceNotFoundException | UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TaskOperationException("An unexpected error occurred while updating task", e);
        }
    }

    public void deleteTask(int id,HttpServletRequest request) throws UserNotFoundException,ResourceNotFoundException {
        try {
            String token = jwtService.getTokenFromHeader(request);
            String username = jwtService.extractUsername(token);
            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Task task = taskRepository.findByIdAndUserId(id, user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

            taskRepository.delete(task);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Error occurred while deleting task", e);
        } catch (ResourceNotFoundException | UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TaskOperationException("An unexpected error occurred while deleting task", e);
        }
    }


}
