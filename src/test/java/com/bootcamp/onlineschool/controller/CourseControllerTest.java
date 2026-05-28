package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.CourseDTO;
import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.service.CourseService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
@Import(GlobalExceptionHandler.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CourseService courseService;

    @Test
    void createCourse_success() throws Exception {
        CourseDTO dto = new CourseDTO("C1", "Intro", 3, "Dr", 10, 0);
        Course model = new Course(dto.getId(), dto.getCourseName(), dto.getCredits(), dto.getInstructor(), dto.getMaxStudents());

        when(courseService.createCourse(eq(dto.getId()), eq(dto.getCourseName()), eq(dto.getCredits()), eq(dto.getInstructor()), eq(dto.getMaxStudents()))).thenReturn(model);
        when(courseService.toDto(model)).thenReturn(dto);

        mvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/courses/C1"))
                .andExpect(jsonPath("$.id").value("C1"));
    }

    @Test
    void createCourse_validation_returns400() throws Exception {
        CourseDTO dto = new CourseDTO(null, "", 0, "", 0, 0);

        mvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void getAllCourses_empty() throws Exception {
        when(courseService.getAllCourses()).thenReturn(List.of());

        mvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getCourseById_found() throws Exception {
        Course c = new Course("C2", "Found", 2, "I", 5);
        when(courseService.getCourseById("C2")).thenReturn(c);
        when(courseService.toDto(c)).thenReturn(new CourseDTO("C2", "Found", 2, "I", 5, 0));

        mvc.perform(get("/api/courses/C2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("C2"));
    }

    @Test
    void getCourseById_notFound_returns404() throws Exception {
        when(courseService.getCourseById("NX")).thenThrow(new CourseService.CourseNotFoundException("no"));

        mvc.perform(get("/api/courses/NX"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateCourse_success() throws Exception {
        Course existing = new Course("C3", "Old", 2, "I", 5);
        CourseDTO dto = new CourseDTO("C3", "New", 3, "I1", 10, 0);
        when(courseService.getCourseById("C3")).thenReturn(existing);

        mvc.perform(put("/api/courses/C3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName").value("New"));
    }

    @Test
    void updateCourse_notFound_returns404() throws Exception {
        CourseDTO dto = new CourseDTO("C4", "X", 1, "I", 1, 0);
        when(courseService.getCourseById("C4")).thenThrow(new CourseService.CourseNotFoundException("no"));

        mvc.perform(put("/api/courses/C4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCourse_success() throws Exception {
        when(courseService.deleteCourse("C5")).thenReturn(true);

        mvc.perform(delete("/api/courses/C5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCourse_notFound_returns404() throws Exception {
        when(courseService.deleteCourse("NX")).thenReturn(false);

        mvc.perform(delete("/api/courses/NX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void enroll_success() throws Exception {
        when(courseService.enrollStudent("C6")).thenReturn(true);

        mvc.perform(post("/api/courses/C6/enroll"))
                .andExpect(status().isOk());
    }

    @Test
    void enroll_courseFull_returns400() throws Exception {
        when(courseService.enrollStudent("FULL")).thenReturn(false);

        mvc.perform(post("/api/courses/FULL/enroll"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unenroll_success() throws Exception {
        when(courseService.unenrollStudent("C7")).thenReturn(true);

        mvc.perform(post("/api/courses/C7/unenroll"))
                .andExpect(status().isOk());
    }

    @Test
    void getAvailable_returnsFiltered() throws Exception {
        Course c1 = new Course("CA", "A", 1, "I", 2);
        when(courseService.getAvailableCourses()).thenReturn(List.of(c1));
        when(courseService.toDto(any())).thenReturn(new CourseDTO("CA", "A", 1, "I", 2, 0));

        mvc.perform(get("/api/courses/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void search_filtersByNameAndCredits() throws Exception {
        Course c = new Course("CS", "SearchMe", 4, "I", 10);
        when(courseService.getAllCourses()).thenReturn(List.of(c));
        when(courseService.toDto(any())).thenReturn(new CourseDTO("CS", "SearchMe", 4, "I", 10, 0));

        mvc.perform(get("/api/courses/search").param("name", "search").param("minCredits", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("CS"));
    }
}
