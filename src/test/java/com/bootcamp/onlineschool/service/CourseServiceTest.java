package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CourseService tests demonstrating Spring Boot service testing
 * 
 * Demonstrates:
 * - @SpringBootTest for integration testing
 * - Service dependency injection
 * - Testing business logic
 * - Exception handling in services
 */
@SpringBootTest
@DisplayName("CourseService Tests")
public class CourseServiceTest {
    
    @Autowired
    private CourseService courseService;
    
    @BeforeEach
    public void setUp() {
        // Clear courses before each test by deleting all existing courses
        courseService.getAllCourses().stream()
            .map(c -> c.getId())
            .forEach(id -> courseService.deleteCourse(id));
    }
    
    @Test
    @DisplayName("Should create course successfully")
    public void testCreateCourse() {
        Course course = new Course("Java Basics", "Introduction to Java", 3, 30);
        Course created = courseService.createCourse(course);
        
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Java Basics", created.getName());
        assertEquals(1, courseService.getTotalCourses());
    }
    
    @Test
    @DisplayName("Should get course by ID")
    public void testGetCourseById() {
        Course course = new Course("Java Basics", "Introduction to Java", 3, 30);
        Course created = courseService.createCourse(course);
        
        Course found = courseService.getCourseById(created.getId());
        assertNotNull(found);
        assertEquals("Java Basics", found.getName());
    }
    
    @Test
    @DisplayName("Should throw exception when course not found")
    public void testGetNonExistentCourse() {
        assertThrows(CourseService.CourseNotFoundException.class, 
            () -> courseService.getCourseById(999L));
    }
    
    @Test
    @DisplayName("Should get all courses")
    public void testGetAllCourses() {
        courseService.createCourse(new Course("Java Basics", "Intro", 3, 30));
        courseService.createCourse(new Course("Advanced Java", "Advanced", 4, 25));
        courseService.createCourse(new Course("Web Development", "Web", 3, 20));
        
        List<Course> courses = courseService.getAllCourses();
        assertEquals(3, courses.size());
    }
    
    @Test
    @DisplayName("Should delete course")
    public void testDeleteCourse() {
        Course course = new Course("Java Basics", "Intro", 3, 30);
        Course created = courseService.createCourse(course);
        assertEquals(1, courseService.getTotalCourses());
        
        courseService.deleteCourse(created.getId());
        assertEquals(0, courseService.getTotalCourses());
    }
}
