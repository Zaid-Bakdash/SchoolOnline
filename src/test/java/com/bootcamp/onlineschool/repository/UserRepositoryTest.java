package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private Student testStudent;
    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        testStudent = new Student("STU001", "John Doe", "john.doe@example.com");
        testStudent.setEnrollmentDate(LocalDate.of(2023, 1, 15));
        testTeacher = new Teacher("EMP001", "Jane Smith", "jane.smith@example.com", "Computer Science", 5, 75000.0, LocalDate.of(2020, 8, 1));
        
        entityManager.persistAndFlush(testStudent);
        entityManager.persistAndFlush(testTeacher);
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John Doe");
        assertThat(foundUser.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailDoesNotExist() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        // When
        boolean exists = userRepository.existsByEmail("jane.smith@example.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // When
        var users = userRepository.findAll();

        // Then
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getEmail)
                .containsExactlyInAnyOrder("john.doe@example.com", "jane.smith@example.com");
    }
}