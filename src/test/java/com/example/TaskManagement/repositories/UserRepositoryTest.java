package com.example.TaskManagement.repositories;

import com.example.TaskManagement.models.auth.Role;
import com.example.TaskManagement.models.auth.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Clean the repository before each test
        userRepository.deleteAll();

        // Add test users
        User user1 = User.builder()
                .username("user1")
                .password("password1")
                .role(Role.USER)
                .build();

        User user2 = User.builder()
                .username("user2")
                .password("password2")
                .role(Role.ADMIN)
                .build();

        // Save users to the repository
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void shouldFindUserByUsername() {
        // Test the findByUsername method
        Optional<User> user = userRepository.findByUsername("user1");

        // Assertions
        assertTrue(user.isPresent());
        assertEquals("user1", user.get().getUsername());
        assertEquals(Role.USER, user.get().getRole());
    }

    @Test
    void shouldReturnEmptyForInvalidUsername() {
        // Test the findByUsername method with an invalid username
        Optional<User> user = userRepository.findByUsername("nonexistent");

        // Assertions
        assertTrue(user.isEmpty());
    }

    @Test
    void shouldSaveUser() {
        // Create a new user for testing save
        User user3 = User.builder()
                .username("user3")
                .password("password3")
                .role(Role.USER)
                .build();

        // Save the user
        User savedUser = userRepository.save(user3);

        // Assertions
        assertNotNull(savedUser);
        assertEquals("user3", savedUser.getUsername());
        assertEquals(Role.USER, savedUser.getRole());
        assertTrue(savedUser.getId() > 0); // Ensure ID is auto-generated and not 0
    }
}
