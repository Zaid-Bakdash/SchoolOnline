package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CourseService demonstrates Spring Boot service with JPA
 *
 * Demonstrates:
 * - @Service annotation
 * - JPA repository usage
 * - Business logic methods
 * - Transaction management
 * - Exception handling
 */
@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Create a new course
     */
    public Course createCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        return courseRepository.save(course);
    }

    /**
     * Get course by ID
     */
    @Transactional(readOnly = true)
    public Course getCourseById(Long id) {
        return courseRepository.findByIdWithEnrollments(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + id));
    }

    /**
     * Get all courses
     */
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Find courses by name
     */
    @Transactional(readOnly = true)
    public List<Course> findCoursesByName(String name) {
        return courseRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Find courses by credits
     */
    @Transactional(readOnly = true)
    public List<Course> findCoursesByCredits(int credits) {
        return courseRepository.findByCredits(credits);
    }

    /**
     * Update course
     */
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = getCourseById(id);
        course.setName(courseDetails.getName());
        course.setDescription(courseDetails.getDescription());
        course.setCredits(courseDetails.getCredits());
        course.setDuration(courseDetails.getDuration());
        return courseRepository.save(course);
    }

    /**
     * Delete course
     */
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException("Course not found: " + id);
        }
        courseRepository.deleteById(id);
    }

    /**
     * Get total number of courses
     */
    @Transactional(readOnly = true)
    public long getTotalCourses() {
        return courseRepository.count();
    }

    /**
     * Custom exception for course not found
     */
    public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(String message) {
            super(message);
        }
    }
}
