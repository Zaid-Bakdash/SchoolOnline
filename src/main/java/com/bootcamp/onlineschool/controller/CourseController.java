package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Course operations
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Get all courses
     */
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    /**
     * Get course by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (CourseService.CourseNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a new course
     */
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        try {
            Course createdCourse = courseService.createCourse(course);
            return ResponseEntity.ok(createdCourse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update a course
     */
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        try {
            Course updatedCourse = courseService.updateCourse(id, course);
            return ResponseEntity.ok(updatedCourse);
        } catch (CourseService.CourseNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete a course
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (CourseService.CourseNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Find courses by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Course>> findCoursesByName(@RequestParam String name) {
        List<Course> courses = courseService.findCoursesByName(name);
        return ResponseEntity.ok(courses);
    }

    /**
     * Find courses by credits
     */
    @GetMapping("/credits/{credits}")
    public ResponseEntity<List<Course>> findCoursesByCredits(@PathVariable int credits) {
        List<Course> courses = courseService.findCoursesByCredits(credits);
        return ResponseEntity.ok(courses);
    }
}