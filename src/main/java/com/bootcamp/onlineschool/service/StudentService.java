package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Student;
import com.bootcamp.onlineschool.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * StudentService demonstrates Spring Boot service layer with JPA
 *
 * Demonstrates:
 * - @Service annotation for dependency injection
 * - JPA repository usage
 * - Business logic encapsulation
 * - Transaction management
 * - Exception handling
 */
@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    /**
     * Constructor injection - Spring automatically injects StudentRepository
     */
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Create a new student
     */
    public Student createStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new StudentAlreadyExistsException("Student with ID " + student.getStudentId() + " already exists");
        }
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new StudentAlreadyExistsException("Student with email " + student.getEmail() + " already exists");
        }
        return studentRepository.save(student);
    }

    /**
     * Get all students
     */
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Find student by ID
     */
    @Transactional(readOnly = true)
    public Student findStudentById(Long id) {
        return studentRepository.findByIdWithEnrollments(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found: " + id));
    }

    /**
     * Find student by studentId
     */
    @Transactional(readOnly = true)
    public Student findStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found: " + studentId));
    }

    /**
     * Find students by name
     */
    @Transactional(readOnly = true)
    public List<Student> findStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Update student
     */
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = findStudentById(id);
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        student.setStudentId(studentDetails.getStudentId());
        student.setEnrollmentDate(studentDetails.getEnrollmentDate());
        return studentRepository.save(student);
    }

    /**
     * Delete student
     */
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student not found: " + id);
        }
        studentRepository.deleteById(id);
    }

    /**
     * Check if student exists by studentId
     */
    @Transactional(readOnly = true)
    public boolean existsByStudentId(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }

    /**
     * Get total number of students
     */
    @Transactional(readOnly = true)
    public long getTotalStudents() {
        return studentRepository.count();
    }

    /**
     * Custom exception for student not found
     */
    public static class StudentNotFoundException extends RuntimeException {
        public StudentNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Custom exception for student already exists
     */
    public static class StudentAlreadyExistsException extends RuntimeException {
        public StudentAlreadyExistsException(String message) {
            super(message);
        }
    }
}
