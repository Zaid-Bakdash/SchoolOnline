package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.TeacherRegistry;
import com.bootcamp.onlineschool.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TeacherService tests demonstrating Spring Boot testing
 *
 * Demonstrates:
 * - @SpringBootTest annotation
 * - Dependency injection in tests
 * - Service layer testing
 * - Integration testing with Spring context
 */
@SpringBootTest
@DisplayName("TeacherService Tests")
public class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRegistry teacherRegistry;

    @BeforeEach
    public void setUp() {
        // Clear registry before each test
        teacherRegistry.clear();
    }

    @Test
    @DisplayName("Should add teacher successfully")
    public void testAddTeacher() {
        Teacher teacher = new Teacher("TCH001", "Dr. Smith", "smith@school.edu", "Computer Science");
        teacherService.addTeacher(teacher);

        assertEquals(1, teacherService.getTotalTeachers());
    }

    @Test
    @DisplayName("Should throw exception when adding null teacher")
    public void testAddNullTeacher() {
        assertThrows(IllegalArgumentException.class, () -> teacherService.addTeacher(null));
    }

    @Test
    @DisplayName("Should find teacher by ID")
    public void testFindTeacherById() {
        Teacher teacher = new Teacher("TCH001", "Dr. Smith", "smith@school.edu", "Computer Science");
        teacherService.addTeacher(teacher);

        Teacher found = teacherService.findTeacherById("TCH001");
        assertNotNull(found);
        assertEquals("Dr. Smith", found.getName());
    }

    @Test
    @DisplayName("Should throw exception when teacher not found")
    public void testFindNonExistentTeacher() {
        assertThrows(TeacherService.TeacherNotFoundException.class,
            () -> teacherService.findTeacherById("NONEXISTENT"));
    }

    @Test
    @DisplayName("Should get all teachers sorted by name")
    public void testGetAllTeachers() {
        teacherService.addTeacher(new Teacher("TCH003", "Dr. Charlie", "charlie@school.edu", "Mathematics"));
        teacherService.addTeacher(new Teacher("TCH001", "Dr. Alice", "alice@school.edu", "Computer Science"));
        teacherService.addTeacher(new Teacher("TCH002", "Dr. Bob", "bob@school.edu", "Physics"));

        List<Teacher> teachers = teacherService.getAllTeachers();
        assertEquals(3, teachers.size());
        assertEquals("Dr. Alice", teachers.get(0).getName());
        assertEquals("Dr. Bob", teachers.get(1).getName());
        assertEquals("Dr. Charlie", teachers.get(2).getName());
    }

    @Test
    @DisplayName("Should find teachers by name")
    public void testFindTeachersByName() {
        teacherService.addTeacher(new Teacher("TCH001", "Dr. Alice Johnson", "alice@school.edu", "Computer Science"));
        teacherService.addTeacher(new Teacher("TCH002", "Dr. Bob Smith", "bob@school.edu", "Physics"));
        teacherService.addTeacher(new Teacher("TCH003", "Dr. Charlie Brown", "charlie@school.edu", "Mathematics"));

        List<Teacher> results = teacherService.findTeachersByName("Charlie");
        assertEquals(1, results.size());
        assertEquals("Dr. Charlie Brown", results.get(0).getName());
    }

    @Test
    @DisplayName("Should get teachers by department")
    public void testGetTeachersByDepartment() {
        teacherService.addTeacher(new Teacher("TCH001", "Dr. Alice", "alice@school.edu", "Computer Science"));
        teacherService.addTeacher(new Teacher("TCH002", "Dr. Bob", "bob@school.edu", "Computer Science"));
        teacherService.addTeacher(new Teacher("TCH003", "Dr. Charlie", "charlie@school.edu", "Mathematics"));

        List<Teacher> csTeachers = teacherService.getTeachersByDepartment("Computer Science");
        assertEquals(2, csTeachers.size());

        List<Teacher> mathTeachers = teacherService.getTeachersByDepartment("Mathematics");
        assertEquals(1, mathTeachers.size());
    }

    @Test
    @DisplayName("Should update teacher information")
    public void testUpdateTeacher() {
        Teacher teacher = new Teacher("TCH001", "Dr. Smith", "smith@school.edu", "Computer Science");
        teacherService.addTeacher(teacher);

        Teacher updatedTeacher = new Teacher("TCH001", "Dr. Smith Jr.", "smithjr@school.edu", "Computer Science", "AI");
        boolean updated = teacherService.updateTeacher("TCH001", updatedTeacher);
        assertTrue(updated);

        Teacher found = teacherService.findTeacherById("TCH001");
        assertEquals("Dr. Smith Jr.", found.getName());
        assertEquals("smithjr@school.edu", found.getEmail());
        assertEquals("AI", found.getSpecialization());
    }

    @Test
    @DisplayName("Should remove teacher")
    public void testRemoveTeacher() {
        teacherService.addTeacher(new Teacher("TCH001", "Dr. Smith", "smith@school.edu", "Computer Science"));
        assertEquals(1, teacherService.getTotalTeachers());

        boolean removed = teacherService.removeTeacher("TCH001");
        assertTrue(removed);
        assertEquals(0, teacherService.getTotalTeachers());
    }
}