package com.example.TaskManagement.repositories;

import com.example.TaskManagement.models.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        // Clean the repository before each test
        taskRepository.deleteAll();

        // Add test data to the repository
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Test Task 1");
        task1.setPriority(1);
        task1.setDone(false);
        task1.setDueDate(LocalDate.of(2025, 5, 15));
        task1.setUserId(1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Test Task 2");
        task2.setPriority(2);
        task2.setDone(true);
        task2.setDueDate(LocalDate.of(2025, 6, 20));
        task2.setUserId(1);

        // Save tasks to the repository
        taskRepository.save(task1);
        taskRepository.save(task2);
    }

    @Test
    void shouldFindTasksByUserId() {
        // Test the repository method
        List<Task> tasks = taskRepository.findByUserId(1);

        // Assertions
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().allMatch(task -> task.getUserId() == 1));
    }

    @Test
    void shouldFindTaskByIdAndUserId() {
        // Test the repository method
        Optional<Task> task = taskRepository.findByIdAndUserId(1, 1);

        // Assertions
        assertTrue(task.isPresent());
        assertEquals("Task 1", task.get().getTitle());
    }

    @Test
    void shouldReturnEmptyForInvalidUserId() {
        // Test the repository method with an invalid userId
        Optional<Task> task = taskRepository.findByIdAndUserId(1, 999);

        // Assertions
        assertTrue(task.isEmpty());
    }

    @Test
    void shouldSaveTask() {
        // Create a new Task object for testing save
        Task task3 = new Task();
        task3.setTitle("Task 3");
        task3.setDescription("Test Task 3");
        task3.setPriority(3);
        task3.setDone(false);
        task3.setDueDate(LocalDate.of(2025, 7, 25));
        task3.setUserId(2);

        // Save the task
        Task savedTask = taskRepository.save(task3);

        // Assertions
        assertNotNull(savedTask);
        assertEquals("Task 3", savedTask.getTitle());
        assertEquals(2, savedTask.getUserId());
        assertTrue(savedTask.getId() > 0); // Ensure ID is auto-generated and not 0
    }
}
