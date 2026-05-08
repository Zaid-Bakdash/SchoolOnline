package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StudentService tests demonstrating Spring Boot testing
 * 
 * Demonstrates:
 * - @SpringBootTest annotation
 * - Dependency injection in tests
 * - Service layer testing
 * - Integration testing with Spring context
 */
@SpringBootTest
@DisplayName("StudentService Tests")
public class StudentServiceTest {
    
    @Autowired
    private StudentService studentService;
    
    @BeforeEach
    public void setUp() {
        // Clear students before each test
        studentService.getAllStudents().stream()
            .map(s -> s.getId())
            .forEach(id -> studentService.deleteStudent(id));
    }
    
    @Test
    @DisplayName("Should add student successfully")
    public void testAddStudent() {
        Student student = new Student("STU001", "Alice", "alice@school.edu", LocalDate.now());
        studentService.createStudent(student);
        
        assertEquals(1, studentService.getTotalStudents());
    }
    
    @Test
    @DisplayName("Should throw exception when adding null student")
    public void testAddNullStudent() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(null));
    }
    
    @Test
    @DisplayName("Should find student by ID")
    public void testFindStudentById() {
        Student student = new Student("STU001", "Alice", "alice@school.edu", LocalDate.now());
        Student created = studentService.createStudent(student);
        
        Student found = studentService.findStudentById(created.getId());
        assertNotNull(found);
        assertEquals("Alice", found.getName());
    }
    
    @Test
    @DisplayName("Should throw exception when student not found")
    public void testFindNonExistentStudent() {
        assertThrows(StudentService.StudentNotFoundException.class, 
            () -> studentService.findStudentById(999L));
    }
    
    @Test
    @DisplayName("Should get all students sorted by name")
    public void testGetAllStudents() {
        studentService.createStudent(new Student("STU003", "Charlie", "charlie@school.edu", LocalDate.now()));
        studentService.createStudent(new Student("STU001", "Alice", "alice@school.edu", LocalDate.now()));
        studentService.createStudent(new Student("STU002", "Bob", "bob@school.edu", LocalDate.now()));
        
        List<Student> students = studentService.getAllStudents();
        assertEquals(3, students.size());
    }
    
    @Test
    @DisplayName("Should find students by name")
    public void testFindStudentsByName() {
        studentService.createStudent(new Student("STU001", "Alice Johnson", "alice@school.edu", LocalDate.now()));
        studentService.createStudent(new Student("STU002", "Bob Smith", "bob@school.edu", LocalDate.now()));
        studentService.createStudent(new Student("STU003", "Charlie Brown", "charlie@school.edu", LocalDate.now()));
        
        List<Student> results = studentService.findStudentsByName("Charlie");
        assertEquals(1, results.size());
        assertEquals("Charlie Brown", results.get(0).getName());
    }
    
    @Test
    @DisplayName("Should remove student")
    public void testRemoveStudent() {
        Student student = new Student("STU001", "Alice", "alice@school.edu", LocalDate.now());
        Student created = studentService.createStudent(student);
        assertEquals(1, studentService.getTotalStudents());
        
        studentService.deleteStudent(created.getId());
        assertEquals(0, studentService.getTotalStudents());
    }
}
