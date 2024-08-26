package com.example.TaskManagement.repositories;

import com.example.TaskManagement.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUserId(int userId);
}