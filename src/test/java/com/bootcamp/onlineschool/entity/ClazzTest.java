package com.bootcamp.onlineschool.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClazzTest {

    private Validator validator;
    private Clazz validClazz;
    private Teacher validTeacher;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        validTeacher = new Teacher(
                "EMP001",
                "Dr. Smith",
                "smith@example.com",
                "Computer Science",
                5,
                75000.0,
                LocalDate.of(2020, 1, 15)
        );
        
        validClazz = new Clazz(
                "Java Programming 101",
                "Fall",
                2024,
                30,
                validTeacher
        );
    }

    @Test
    void testValidClazz() {
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        assertTrue(violations.isEmpty(), "Valid clazz should have no validation errors");
    }

    @Test
    void testClazzNameRequired() {
        validClazz.setName(null);
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Class name is required")));
    }

    @Test
    void testClazzNameBlank() {
        validClazz.setName("");
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Class name is required")));
    }

    @Test
    void testClazzNameTooShort() {
        validClazz.setName("A");
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Class name must be between 2 and 100 characters")));
    }

    @Test
    void testClazzNameTooLong() {
        validClazz.setName("A".repeat(101));
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Class name must be between 2 and 100 characters")));
    }

    @Test
    void testSemesterRequired() {
        validClazz.setSemester(null);
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Semester is required")));
    }

    @Test
    void testSemesterBlank() {
        validClazz.setSemester("");
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Semester is required")));
    }

    @Test
    void testSemesterTooShort() {
        validClazz.setSemester("A");
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Semester must be between 2 and 20 characters")));
    }

    @Test
    void testSemesterTooLong() {
        validClazz.setSemester("A".repeat(21));
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Semester must be between 2 and 20 characters")));
    }

    @Test
    void testYearRequired() {
        validClazz.setYear(null);
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Year is required")));
    }

    @Test
    void testYearMinimumValue() {
        validClazz.setYear(1999);
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Year must be at least 2000")));
    }

    @Test
    void testMaxCapacityRequired() {
        validClazz.setMaxCapacity(null);
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Max capacity is required")));
    }

    @Test
    void testMaxCapacityMinimumValue() {
        validClazz.setMaxCapacity(0);
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Max capacity must be at least 1")));
    }

    @Test
    void testTeacherRequired() {
        validClazz.setTeacher(null);
        Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Teacher is required")));
    }

    @Test
    void testTeacherRelationship() {
        Teacher newTeacher = new Teacher(
                "EMP002",
                "Dr. Johnson",
                "johnson@example.com",
                "Mathematics",
                10,
                85000.0,
                LocalDate.of(2019, 3, 10)
        );
        
        validClazz.setTeacher(newTeacher);
        assertEquals(newTeacher, validClazz.getTeacher());
        assertEquals("Dr. Johnson", validClazz.getTeacher().getName());
        assertEquals("Mathematics", validClazz.getTeacher().getDepartment());
    }

    @Test
    void testConstructorWithAllFields() {
        Teacher teacher = new Teacher("EMP003", "Prof. Brown", "brown@example.com", "Physics", 9, 82000.0, LocalDate.of(2018, 5, 20));
        Clazz clazz = new Clazz("Physics 101", "Spring", 2024, 25, teacher);
        
        assertEquals("Physics 101", clazz.getName());
        assertEquals("Spring", clazz.getSemester());
        assertEquals(2024, clazz.getYear());
        assertEquals(25, clazz.getMaxCapacity());
        assertEquals(teacher, clazz.getTeacher());
    }

    @Test
    void testDefaultConstructor() {
        Clazz clazz = new Clazz();
        assertNotNull(clazz);
        assertNull(clazz.getName());
        assertNull(clazz.getSemester());
        assertNull(clazz.getYear());
        assertNull(clazz.getMaxCapacity());
        assertNull(clazz.getTeacher());
    }

    @Test
    void testEqualsAndHashCode() {
        Clazz clazz1 = new Clazz("Math 101", "Fall", 2024, 30, validTeacher);
        Clazz clazz2 = new Clazz("Science 101", "Spring", 2024, 25, validTeacher);
        
        // Classes without IDs should not be equal
        assertNotEquals(clazz1, clazz2);
        
        // Test with same reference
        assertEquals(clazz1, clazz1);
        
        // Test with null
        assertNotEquals(clazz1, null);
        
        // Test with different class
        assertNotEquals(clazz1, "not a clazz");
    }

    @Test
    void testToString() {
        String toString = validClazz.toString();
        assertTrue(toString.contains("Clazz{"));
        assertTrue(toString.contains("name='Java Programming 101'"));
        assertTrue(toString.contains("semester='Fall'"));
        assertTrue(toString.contains("year=2024"));
        assertTrue(toString.contains("maxCapacity=30"));
        assertTrue(toString.contains("teacher=Dr. Smith"));
    }

    @Test
    void testToStringWithNullTeacher() {
        validClazz.setTeacher(null);
        String toString = validClazz.toString();
        assertTrue(toString.contains("teacher=null"));
    }

    @Test
    void testSettersAndGetters() {
        Clazz clazz = new Clazz();
        
        clazz.setName("Test Class");
        assertEquals("Test Class", clazz.getName());
        
        clazz.setSemester("Summer");
        assertEquals("Summer", clazz.getSemester());
        
        clazz.setYear(2025);
        assertEquals(2025, clazz.getYear());
        
        clazz.setMaxCapacity(40);
        assertEquals(40, clazz.getMaxCapacity());
        
        clazz.setTeacher(validTeacher);
        assertEquals(validTeacher, clazz.getTeacher());
        
        clazz.setId(1L);
        assertEquals(1L, clazz.getId());
    }

    @Test
    void testValidSemesterValues() {
        String[] validSemesters = {"Fall", "Spring", "Summer", "Winter"};
        
        for (String semester : validSemesters) {
            validClazz.setSemester(semester);
            Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
            
            // Should not have violations for semester field
            assertFalse(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("semester")));
        }
    }

    @Test
    void testValidYearRange() {
        int[] validYears = {2000, 2024, 2030, 2050};
        
        for (int year : validYears) {
            validClazz.setYear(year);
            Set<ConstraintViolation<Clazz>> violations = validator.validate(validClazz);
            
            // Should not have violations for year field
            assertFalse(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("year")));
        }
    }
}