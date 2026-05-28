package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.model.Student;
import com.bootcamp.onlineschool.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody @Valid StudentDTO dto) {
        Student student = studentService.fromDto(dto);
        studentService.addStudent(student);
        StudentDTO created = studentService.toDto(student);
        URI location = URI.create(String.format("/api/students/%s", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents(@RequestParam(required = false) String name) {
        List<Student> students = (name == null)
                ? studentService.getAllStudents()
                : studentService.findStudentsByName(name);
        List<StudentDTO> dtos = students.stream().map(studentService::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable String id) {
        Student student = studentService.findStudentById(id);
        return ResponseEntity.ok(studentService.toDto(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable String id, @RequestBody @Valid StudentDTO dto) {
        Student existing = studentService.findStudentById(id);
        Student updated = studentService.fromDto(dto);
        // ensure ID consistency
        updated.setStudentId(existing.getStudentId());
        studentService.addStudent(updated);
        return ResponseEntity.ok(studentService.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        boolean removed = studentService.removeStudent(id);
        if (!removed) {
            throw new StudentService.StudentNotFoundException("Student not found: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/high-achievers")
    public ResponseEntity<List<StudentDTO>> getHighAchievers(@RequestParam(defaultValue = "3.5") Double gpa) {
        List<Student> students = studentService.getHighAchievers(gpa);
        List<StudentDTO> dtos = students.stream().map(studentService::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
