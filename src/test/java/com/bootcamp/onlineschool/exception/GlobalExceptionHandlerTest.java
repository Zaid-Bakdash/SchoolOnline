package com.bootcamp.onlineschool.exception;

import com.bootcamp.onlineschool.controller.StudentController;
import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private StudentService studentService;

    @Test
    void validationErrors_produceErrorResponse() throws Exception {
        StudentDTO dto = new StudentDTO(null, "", "bad", 6.0);

        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void malformedJson_producesBadRequest() throws Exception {
        String badJson = "{\"id\": "; // truncated

        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON"));
    }

    @Test
    void genericException_produces500() throws Exception {
        StudentDTO dto = new StudentDTO("Sx", "N", "n@x.com", 2.0);
        when(studentService.fromDto(org.mockito.ArgumentMatchers.any())).thenThrow(new RuntimeException("boom"));

        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    void resourceNotFound_handledProperly() throws Exception {
        when(studentService.findStudentById("NX")).thenThrow(new StudentService.StudentNotFoundException("no"));

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/students/NX"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void badRequestException_handledProperly() throws Exception {
        // simulate malformed numeric input by sending invalid gpa type
        String payload = "{\"id\":\"B1\",\"name\":\"B\",\"email\":\"b@x.com\",\"gpa\":\"NaN\"}";

        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }
}
