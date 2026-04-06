package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Student Class Tests")
public class StudentTest {

    private Student student;

    @BeforeEach
    public void setUp() {
        student = new Student("STU100", "Test Student", "test@example.com");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 2.0, 3.5, 4.0})
    @DisplayName("Should accept valid GPA values")
    public void testValidGpaValues(double gpa) {
        student.setGpa(gpa);
        assertEquals(gpa, student.getGpa(), 0.00001, "GPA should be set and retrievable");
    }

    @Test
    @DisplayName("Should throw for invalid GPA values")
    public void testInvalidGpaValues() {
        assertThrows(IllegalArgumentException.class, () -> student.setGpa(-0.1));
        assertThrows(IllegalArgumentException.class, () -> student.setGpa(4.1));
    }

    @Test
    @DisplayName("Should throw for NaN GPA")
    public void testInvalidGpaNaN() {
        assertThrows(IllegalArgumentException.class, () -> student.setGpa(Double.NaN));
    }
}
