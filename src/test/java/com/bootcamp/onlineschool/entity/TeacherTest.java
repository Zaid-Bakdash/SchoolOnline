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

class TeacherTest {

    private Validator validator;
    private Teacher validTeacher;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        validTeacher = new Teacher(
                "EMP001",
                "Dr. Jane Smith",
                "jane.smith@example.com",
                "Computer Science",
                5,
                75000.0,
                LocalDate.of(2020, 3, 15)
        );
    }

    @Test
    void testValidTeacher() {
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        assertTrue(violations.isEmpty(), "Valid teacher should have no validation errors");
    }

    @Test
    void testEmployeeIdRequired() {
        validTeacher.setTeacherId(null);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Employee ID is required")));
    }

    @Test
    void testEmployeeIdBlank() {
        validTeacher.setTeacherId("");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Employee ID is required")));
    }

    @Test
    void testEmployeeIdTooShort() {
        validTeacher.setTeacherId("AB");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Employee ID must be between 3 and 20 characters")));
    }

    @Test
    void testEmployeeIdTooLong() {
        validTeacher.setTeacherId("A".repeat(21));
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Employee ID must be between 3 and 20 characters")));
    }

    @Test
    void testDepartmentRequired() {
        validTeacher.setDepartment(null);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Department is required")));
    }

    @Test
    void testDepartmentBlank() {
        validTeacher.setDepartment("");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Department is required")));
    }

    @Test
    void testDepartmentTooShort() {
        validTeacher.setDepartment("A");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Department must be between 2 and 100 characters")));
    }

    @Test
    void testDepartmentTooLong() {
        validTeacher.setDepartment("A".repeat(101));
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Department must be between 2 and 100 characters")));
    }

    @Test
    void testHireDateRequired() {
        validTeacher.setHireDate(null);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Hire date is required")));
    }

    @Test
    void testHireDateInFuture() {
        validTeacher.setHireDate(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Hire date must be in the past")));
    }

    @Test
    void testInheritedValidation() {
        // Test inherited name validation
        validTeacher.setName("");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Name is required")));
    }

    @Test
    void testInheritedEmailValidation() {
        // Test inherited email validation
        validTeacher.setEmail("invalid-email");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(validTeacher);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Email should be valid")));
    }

    @Test
    void testConstructorWithAllFields() {
        Teacher teacher = new Teacher(
                "EMP002",
                "Prof. Bob Johnson",
                "bob@example.com",
                "Mathematics",
                8,
                80000.0,
                LocalDate.of(2019, 8, 1)
        );
        
        assertEquals("Prof. Bob Johnson", teacher.getName());
        assertEquals("bob@example.com", teacher.getEmail());
        assertEquals("EMP002", teacher.getTeacherId());
        assertEquals("Mathematics", teacher.getDepartment());
        assertEquals(LocalDate.of(2019, 8, 1), teacher.getHireDate());
    }

    @Test
    void testDefaultConstructor() {
        Teacher teacher = new Teacher();
        assertNotNull(teacher);
        assertNull(teacher.getName());
        assertNull(teacher.getEmail());
        assertNull(teacher.getTeacherId());
        assertNull(teacher.getDepartment());
        assertNull(teacher.getHireDate());
    }

    @Test
    void testEqualsAndHashCode() {
        Teacher teacher1 = new Teacher(
                "EMP001",
                "Dr. Jane Smith",
                "jane@example.com",
                "Computer Science",
                5,
                75000.0,
                LocalDate.of(2020, 3, 15)
        );
        Teacher teacher2 = new Teacher(
                "EMP001",
                "Prof. John Doe",
                "john@example.com",
                "Mathematics",
                7,
                78000.0,
                LocalDate.of(2019, 1, 1)
        );
        Teacher teacher3 = new Teacher(
                "EMP002",
                "Dr. Alice Brown",
                "alice@example.com",
                "Physics",
                6,
                76000.0,
                LocalDate.of(2021, 5, 10)
        );
        
        // Teachers with same employeeId should be equal
        assertEquals(teacher1, teacher2);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
        
        // Teachers with different employeeId should not be equal
        assertNotEquals(teacher1, teacher3);
    }

    @Test
    void testToString() {
        String toString = validTeacher.toString();
        assertTrue(toString.contains("Teacher{"));
        assertTrue(toString.contains("name='Dr. Jane Smith'"));
        assertTrue(toString.contains("email='jane.smith@example.com'"));
        assertTrue(toString.contains("employeeId='EMP001'"));
        assertTrue(toString.contains("department='Computer Science'"));
        assertTrue(toString.contains("hireDate=2020-03-15"));
    }
}