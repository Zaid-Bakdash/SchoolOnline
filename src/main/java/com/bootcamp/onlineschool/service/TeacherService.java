package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.TeacherRegistry;
import com.bootcamp.onlineschool.model.Teacher;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * TeacherService demonstrates Spring Boot service layer
 *
 * Demonstrates:
 * - @Service annotation for dependency injection
 * - Business logic encapsulation
 * - Service layer pattern
 * - Dependency injection
 */
@Service
public class TeacherService {

    private final TeacherRegistry teacherRegistry;

    /**
     * Constructor injection - Spring automatically injects TeacherRegistry
     */
    public TeacherService(TeacherRegistry teacherRegistry) {
        this.teacherRegistry = teacherRegistry;
    }

    /**
     * Add a new teacher
     */
    public void addTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }
        teacherRegistry.addTeacher(teacher);
    }

    /**
     * Get all teachers
     */
    public List<Teacher> getAllTeachers() {
        return teacherRegistry.getAllTeachersSortedByName();
    }

    /**
     * Find teacher by ID
     */
    public Teacher findTeacherById(String teacherId) {
        Teacher teacher = teacherRegistry.findTeacherById(teacherId);
        if (teacher == null) {
            throw new TeacherNotFoundException("Teacher not found: " + teacherId);
        }
        return teacher;
    }

    /**
     * Find teachers by name
     */
    public List<Teacher> findTeachersByName(String name) {
        return teacherRegistry.findTeachersByName(name);
    }

    /**
     * Get teachers by department
     */
    public List<Teacher> getTeachersByDepartment(String department) {
        return teacherRegistry.getTeachersByDepartment(department);
    }

    /**
     * Update teacher information
     */
    public boolean updateTeacher(String teacherId, Teacher updatedTeacher) {
        return teacherRegistry.updateTeacher(teacherId, updatedTeacher);
    }

    /**
     * Remove a teacher
     */
    public boolean removeTeacher(String teacherId) {
        return teacherRegistry.removeTeacher(teacherId);
    }

    /**
     * Get total number of teachers
     */
    public int getTotalTeachers() {
        return teacherRegistry.getTeacherCount();
    }

    /**
     * Custom exception for teacher not found
     */
    public static class TeacherNotFoundException extends RuntimeException {
        public TeacherNotFoundException(String message) {
            super(message);
        }
    }
}