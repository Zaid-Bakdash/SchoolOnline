package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.model.Student;
import com.bootcamp.onlineschool.service.StudentService;
import com.bootcamp.onlineschool.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
@Import(GlobalExceptionHandler.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private StudentService studentService;

    @Test
    void createStudent_success() throws Exception {
        StudentDTO dto = new StudentDTO("S1", "John", "john@school.edu", 3.5);
        Student model = new Student(dto.getId(), dto.getName(), dto.getEmail(), dto.getGpa());

        when(studentService.fromDto(any())).thenReturn(model);
        doNothing().when(studentService).addStudent(any());
        when(studentService.toDto(any())).thenReturn(dto);

        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/students/S1"))
                .andExpect(jsonPath("$.id").value("S1"))
                .andExpect(jsonPath("$.email").value("john@school.edu"));
    }

    @Test
    void createStudent_invalidEmail_returns400() throws Exception {
        StudentDTO dto = new StudentDTO("S2", "Jane", "bad-email", 3.0);

        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void createStudent_gpaOutOfRange_returns400() throws Exception {
        StudentDTO dto = new StudentDTO("S3", "BadGpa", "g@x.com", 5.0);

        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void getAllStudents_empty() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of());

        mvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAllStudents_withNameFilter() throws Exception {
        Student s = new Student("S4", "FilterMe", "f@x.com", 3.0);
        when(studentService.findStudentsByName(eq("FilterMe"))).thenReturn(List.of(s));
        when(studentService.toDto(any())).thenReturn(new StudentDTO(s.getStudentId(), s.getName(), s.getEmail(), s.getGpa()));

        mvc.perform(get("/api/students").param("name", "FilterMe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("S4"));
    }

    @Test
    void getStudentById_found() throws Exception {
        Student s = new Student("S5", "Found", "found@x.com", 3.2);
        when(studentService.findStudentById("S5")).thenReturn(s);
        when(studentService.toDto(s)).thenReturn(new StudentDTO("S5", "Found", "found@x.com", 3.2));

        mvc.perform(get("/api/students/S5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("S5"));
    }

    @Test
    void getStudentById_notFound_returns404() throws Exception {
        when(studentService.findStudentById("NX")).thenThrow(new StudentService.StudentNotFoundException("not"));

        mvc.perform(get("/api/students/NX"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateStudent_success() throws Exception {
        Student existing = new Student("S6", "Old", "o@x.com", 2.0);
        StudentDTO dto = new StudentDTO("S6", "New", "n@x.com", 3.0);
        when(studentService.findStudentById("S6")).thenReturn(existing);
        when(studentService.fromDto(any())).thenReturn(new Student(dto.getId(), dto.getName(), dto.getEmail(), dto.getGpa()));
        when(studentService.toDto(any())).thenReturn(dto);

        mvc.perform(put("/api/students/S6")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void updateStudent_notFound_returns404() throws Exception {
        StudentDTO dto = new StudentDTO("S7", "X", "x@x.com", 2.5);
        when(studentService.findStudentById("S7")).thenThrow(new StudentService.StudentNotFoundException("no"));

        mvc.perform(put("/api/students/S7")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudent_success() throws Exception {
        when(studentService.removeStudent("S8")).thenReturn(true);

        mvc.perform(delete("/api/students/S8"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteStudent_notFound_returns404() throws Exception {
        when(studentService.removeStudent("NX")).thenReturn(false);

        mvc.perform(delete("/api/students/NX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHighAchievers_returnsList() throws Exception {
        Student s = new Student("S9", "HA", "ha@x.com", 3.9);
        when(studentService.getHighAchievers(3.5)).thenReturn(List.of(s));
        when(studentService.toDto(any())).thenReturn(new StudentDTO(s.getStudentId(), s.getName(), s.getEmail(), s.getGpa()));

        mvc.perform(get("/api/students/high-achievers").param("gpa", "3.5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("S9"));
    }
}
