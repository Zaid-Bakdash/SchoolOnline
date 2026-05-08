package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.model.Student;
import com.bootcamp.onlineschool.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Student operations
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Get all students
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * Get student by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        try {
            Student student = studentService.findStudentById(id);
            return ResponseEntity.ok(student);
        } catch (StudentService.StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get student by studentId
     */
    @GetMapping("/studentId/{studentId}")
    public ResponseEntity<Student> getStudentByStudentId(@PathVariable String studentId) {
        try {
            Student student = studentService.findStudentByStudentId(studentId);
            return ResponseEntity.ok(student);
        } catch (StudentService.StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a new student
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.ok(createdStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update a student
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        try {
            Student updatedStudent = studentService.updateStudent(id, student);
            return ResponseEntity.ok(updatedStudent);
        } catch (StudentService.StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete a student
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (StudentService.StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Check if student exists by studentId
     */
    @GetMapping("/exists/studentId/{studentId}")
    public ResponseEntity<Boolean> existsByStudentId(@PathVariable String studentId) {
        boolean exists = studentService.existsByStudentId(studentId);
        return ResponseEntity.ok(exists);
    }

    /**
     * Find students by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Student>> findStudentsByName(@RequestParam String name) {
        List<Student> students = studentService.findStudentsByName(name);
        return ResponseEntity.ok(students);
    }
}