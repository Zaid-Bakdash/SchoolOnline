package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.CourseDTO;
import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody @Valid CourseDTO dto) {
        Course created = courseService.createCourse(dto.getId(), dto.getCourseName(), dto.getCredits(), dto.getInstructor(), dto.getMaxStudents());
        CourseDTO out = courseService.toDto(created);
        URI location = URI.create(String.format("/api/courses/%s", out.getId()));
        return ResponseEntity.created(location).body(out);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        List<CourseDTO> dtos = courses.stream().map(courseService::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable String id) {
        Course c = courseService.getCourseById(id);
        return ResponseEntity.ok(courseService.toDto(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable String id, @RequestBody @Valid CourseDTO dto) {
        Course existing = courseService.getCourseById(id);
        // update fields
        existing = new Course(existing.getCourseId(), dto.getCourseName(), dto.getCredits(), dto.getInstructor(), dto.getMaxStudents());
        // not preserving enrolledStudents here
        CourseDTO out = courseService.toDto(existing);
        return ResponseEntity.ok(out);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        boolean removed = courseService.deleteCourse(id);
        if (!removed) {
            throw new CourseService.CourseNotFoundException("Course not found: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<Void> enroll(@PathVariable String id) {
        boolean ok = courseService.enrollStudent(id);
        if (!ok) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unenroll")
    public ResponseEntity<Void> unenroll(@PathVariable String id) {
        boolean ok = courseService.unenrollStudent(id);
        if (!ok) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<CourseDTO>> getAvailable() {
        List<Course> courses = courseService.getAvailableCourses();
        List<CourseDTO> dtos = courses.stream().map(courseService::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/instructor/{name}")
    public ResponseEntity<List<CourseDTO>> getByInstructor(@PathVariable String name) {
        List<Course> courses = courseService.getAllCourses().stream()
                .filter(c -> c.getInstructor().equalsIgnoreCase(name))
                .toList();
        List<CourseDTO> dtos = courses.stream().map(courseService::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> search(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) Integer minCredits) {
        List<Course> courses = courseService.getAllCourses();
        List<Course> filtered = courses.stream()
                .filter(c -> name == null || c.getCourseName().toLowerCase().contains(name.toLowerCase()))
                .filter(c -> minCredits == null || c.getCredits() >= minCredits)
                .toList();
        List<CourseDTO> dtos = filtered.stream().map(courseService::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
