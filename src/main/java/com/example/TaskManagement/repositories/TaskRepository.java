package com.example.TaskManagement.repositories;

import com.example.TaskManagement.models.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUserId(int userId);
    Optional<Task> findByIdAndUserId(int id, Integer userId);
}