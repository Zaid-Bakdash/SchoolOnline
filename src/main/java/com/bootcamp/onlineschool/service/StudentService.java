package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.StudentRegistry;
import com.bootcamp.onlineschool.model.Student;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * StudentService demonstrates Spring Boot service layer
 * 
 * Demonstrates:
 * - @Service annotation for dependency injection
 * - Business logic encapsulation
 * - Service layer pattern
 * - Dependency injection
 */
@Service
public class StudentService {
    
    private final StudentRegistry studentRegistry;
    
    /**
     * Constructor injection - Spring automatically injects StudentRegistry
     */
    public StudentService(StudentRegistry studentRegistry) {
        this.studentRegistry = studentRegistry;
    }
    
    /**
     * Add a new student
     */
    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        studentRegistry.addStudent(student);
    }
    
    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return studentRegistry.getAllStudentsSortedByName();
    }
    
    /**
     * Find student by ID
     */
    public Student findStudentById(String studentId) {
        Student student = studentRegistry.findStudentById(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found: " + studentId);
        }
        return student;
    }
    
    /**
     * Find students by name
     */
    public List<Student> findStudentsByName(String name) {
        return studentRegistry.findStudentsByName(name);
    }
    
    /**
     * Get students with high GPA
     */
    public List<Student> getHighAchievers(double gpaThreshold) {
        return studentRegistry.getStudentsWithHighGpa(gpaThreshold);
    }
    
    /**
     * Remove a student
     */
    public boolean removeStudent(String studentId) {
        return studentRegistry.removeStudent(studentId);
    }
    
    /**
     * Get total number of students
     */
    public int getTotalStudents() {
        return studentRegistry.getStudentCount();
    }
    
    /**
     * Get average GPA
     */
    public double getAverageGpa() {
        return studentRegistry.getAverageGpa();
    }
    
    // DTO conversion helpers
    public com.bootcamp.onlineschool.dto.StudentDTO toDto(Student s) {
        if (s == null) return null;
        return new com.bootcamp.onlineschool.dto.StudentDTO(
                s.getStudentId(), s.getName(), s.getEmail(), s.getGpa());
    }
    
    public Student fromDto(com.bootcamp.onlineschool.dto.StudentDTO dto) {
        if (dto == null) return null;
        Double gpa = dto.getGpa() == null ? 0.0 : dto.getGpa();
        Student s = new Student(dto.getId(), dto.getName(), dto.getEmail(), gpa);
        return s;
    }
    
    /**
     * Custom exception for student not found
     */
    public static class StudentNotFoundException extends RuntimeException {
        public StudentNotFoundException(String message) {
            super(message);
        }
    }
}
