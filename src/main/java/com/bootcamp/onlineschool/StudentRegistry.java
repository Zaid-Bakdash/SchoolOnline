package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Student;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StudentRegistry class demonstrating:
 * - Collections (ArrayList, HashMap)
 * - Iteration (for-each, Iterator)
 * - Streams API
 * - Lambda expressions
 * - Sorting
 */
public class StudentRegistry {
    private List<Student> students;
    
    public StudentRegistry() {
        this.students = new ArrayList<>();
    }
    
    /**
     * Add a student to the registry
     */
    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (!student.isValidEmail()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        students.add(student);
    }
    
    /**
     * Remove a student by ID
     */
    public boolean removeStudent(String studentId) {
        return students.removeIf(s -> s.getStudentId().equals(studentId));
    }
    
    /**
     * Find a student by ID
     */
    public Student findStudentById(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Find students by name (partial match)
     */
    public List<Student> findStudentsByName(String name) {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all students sorted by name
     */
    public List<Student> getAllStudentsSortedByName() {
        return students.stream()
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }
    
    /**
     * Display all students
     */
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in registry");
            return;
        }
        students.forEach(System.out::println);
    }
    
    /**
     * Get total number of students
     */
    public int getStudentCount() {
        return students.size();
    }
    
    /**
     * Clear all students
     */
    public void clear() {
        students.clear();
    }
}
