package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Course;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * CourseService demonstrates Spring Boot service with in-memory storage
 * 
 * Demonstrates:
 * - @Service annotation
 * - In-memory data management
 * - Business logic methods
 * - Exception handling
 */
@Service
public class CourseService {
    
    private final Map<String, Course> courses = new HashMap<>();
    
    /**
     * Create a new course
     */
    public Course createCourse(String courseId, String courseName, int credits, 
                               String instructor, int maxStudents) {
        if (courses.containsKey(courseId)) {
            throw new CourseAlreadyExistsException("Course already exists: " + courseId);
        }
        
        Course course = new Course(courseId, courseName, credits, instructor, maxStudents);
        courses.put(courseId, course);
        return course;
    }
    
    /**
     * Get course by ID
     */
    public Course getCourseById(String courseId) {
        Course course = courses.get(courseId);
        if (course == null) {
            throw new CourseNotFoundException("Course not found: " + courseId);
        }
        return course;
    }
    
    /**
     * Get all courses
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    /**
     * Enroll student in course
     */
    public boolean enrollStudent(String courseId) {
        Course course = getCourseById(courseId);
        return course.enrollStudent();
    }
    
    /**
     * Unenroll student from course
     */
    public boolean unenrollStudent(String courseId) {
        Course course = getCourseById(courseId);
        return course.unenrollStudent();
    }
    
    /**
     * Get available courses (not full)
     */
    public List<Course> getAvailableCourses() {
        return courses.values().stream()
                .filter(c -> !c.isFull())
                .toList();
    }
    
    /**
     * Update course instructor
     */
    public void updateInstructor(String courseId, String newInstructor) {
        Course course = getCourseById(courseId);
        course.setInstructor(newInstructor);
    }
    
    /**
     * Delete course
     */
    public boolean deleteCourse(String courseId) {
        return courses.remove(courseId) != null;
    }
    
    /**
     * Get total number of courses
     */
    public int getTotalCourses() {
        return courses.size();
    }

    // DTO conversion helpers
    public com.bootcamp.onlineschool.dto.CourseDTO toDto(Course c) {
        if (c == null) return null;
        return new com.bootcamp.onlineschool.dto.CourseDTO(
                c.getCourseId(), c.getCourseName(), c.getCredits(), c.getInstructor(), c.getMaxStudents(), c.getEnrolledStudents());
    }

    public Course fromDto(com.bootcamp.onlineschool.dto.CourseDTO dto) {
        if (dto == null) return null;
        Course c = new Course(dto.getId(), dto.getCourseName(), dto.getCredits(), dto.getInstructor(), dto.getMaxStudents());
        // try to set enrolledStudents if provided - Course doesn't have setter for enrolledStudents
        return c;
    }
    
    /**
     * Custom exception for course not found
     */
    public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(String message) {
            super(message);
        }
    }
    
    /**
     * Custom exception for course already exists
     */
    public static class CourseAlreadyExistsException extends RuntimeException {
        public CourseAlreadyExistsException(String message) {
            super(message);
        }
    }
}
