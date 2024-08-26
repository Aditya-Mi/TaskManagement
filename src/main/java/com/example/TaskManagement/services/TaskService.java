package com.example.TaskManagement.services;

import com.example.TaskManagement.UserNotFoundException;
import com.example.TaskManagement.models.Task;
import com.example.TaskManagement.models.auth.User;
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

    public Task saveTask(
            Task task,
            HttpServletRequest request
    ) throws UserNotFoundException {
        String token = jwtService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(token);
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        task.setUserId(user.getId());
        Task taskSaved = taskRepository.save(task);
        log.info("Task saved: {}", taskSaved);
        return taskSaved;
    }

    public Task updateTask(Task task) {
        Task existingTask = taskRepository.findById(task.getId()).get();
        task.setUserId(existingTask.getUserId());
        Task taskSaved = taskRepository.save(task);
        log.info("Task updated: {}", taskSaved);
        return taskSaved;
    }

    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }


}
