package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enrollment Entity tests
 */
@DisplayName("Enrollment Entity Tests")
public class EnrollmentEntityTest {

    private Enrollment enrollment;
    private Student student;
    private Course course;

    @BeforeEach
    public void setUp() {
        student = new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1));
        course = new Course("Java Programming", "Learn Java basics", 3, 30);
        enrollment = new Enrollment(student, course, LocalDate.now(), EnrollmentStatus.ENROLLED);
    }

    @Test
    @DisplayName("Should create valid enrollment")
    public void testCreateValidEnrollment() {
        assertNotNull(enrollment);
        assertEquals(student, enrollment.getStudent());
        assertEquals(course, enrollment.getCourse());
        assertEquals(EnrollmentStatus.ENROLLED, enrollment.getStatus());
        assertNotNull(enrollment.getEnrollmentDate());
    }

    @Test
    @DisplayName("Should set and get all fields")
    public void testSettersAndGetters() {
        enrollment.setGrade("A");
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollment.setCompletionDate(LocalDate.now().plusDays(30));

        assertEquals("A", enrollment.getGrade());
        assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
        assertNotNull(enrollment.getCompletionDate());
    }

    @Test
    @DisplayName("Should complete enrollment with grade")
    public void testCompleteEnrollment() {
        enrollment.complete("B");

        assertEquals("B", enrollment.getGrade());
        assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
        assertNotNull(enrollment.getCompletionDate());
        assertTrue(enrollment.isCompleted());
    }

    @Test
    @DisplayName("Should drop enrollment")
    public void testDropEnrollment() {
        enrollment.drop();

        assertEquals(EnrollmentStatus.DROPPED, enrollment.getStatus());
        assertFalse(enrollment.isActive());
    }

    @Test
    @DisplayName("Should withdraw from enrollment")
    public void testWithdrawEnrollment() {
        enrollment.withdraw();

        assertEquals(EnrollmentStatus.WITHDRAWN, enrollment.getStatus());
        assertFalse(enrollment.isActive());
    }

    @Test
    @DisplayName("Should check if enrollment is active")
    public void testIsActive() {
        assertTrue(enrollment.isActive());

        enrollment.drop();
        assertFalse(enrollment.isActive());
    }

    @Test
    @DisplayName("Should check if enrollment is completed")
    public void testIsCompleted() {
        assertFalse(enrollment.isCompleted());

        enrollment.complete("A");
        assertTrue(enrollment.isCompleted());
    }

    @Test
    @DisplayName("Should validate equals based on student, course, and enrollment date")
    public void testEquals() {
        Enrollment another = new Enrollment(student, course, enrollment.getEnrollmentDate(), EnrollmentStatus.ENROLLED);
        assertEquals(enrollment, another);
    }

    @Test
    @DisplayName("Should generate hashCode based on student, course, and enrollment date")
    public void testHashCode() {
        Enrollment another = new Enrollment(student, course, enrollment.getEnrollmentDate(), EnrollmentStatus.ENROLLED);
        assertEquals(enrollment.hashCode(), another.hashCode());
    }
}