package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced JUnit 5 tests for Course class
 * Demonstrates:
 * - Nested test classes
 * - Parameterized tests
 * - Test display names
 * - Test organization
 */
@DisplayName("Course Class Tests")
public class CourseTest {
    
    private Course course;
    
    @BeforeEach
    public void setUp() {
        course = new Course("CS101", "Introduction to Java", 3, "Dr. Smith", 30);
    }
    
    @Test
    @DisplayName("Should create course with valid data")
    public void testCourseCreation() {
        assertNotNull(course);
        assertEquals("CS101", course.getCourseId());
        assertEquals("Introduction to Java", course.getCourseName());
        assertEquals(3, course.getCredits());
        assertEquals("Dr. Smith", course.getInstructor());
        assertEquals(30, course.getMaxStudents());
        assertEquals(0, course.getEnrolledStudents());
    }

    // Helper assertions for common course state checks (Exercise 5)
    private void assertCourseIsFull(Course course) {
        assertTrue(course.isFull(), "Course should be full");
        assertEquals(0, course.getAvailableSeats(), "Expected 0 available seats when full");
    }
    
    @Nested
    @DisplayName("Enrollment Tests")
    class EnrollmentTests {
        
        @Test
        @DisplayName("Should enroll student successfully")
        public void testEnrollStudent() {
            boolean enrolled = course.enrollStudent();
            assertTrue(enrolled);
            assertEquals(1, course.getEnrolledStudents());
        }
        
        @Test
        @DisplayName("Should not enroll when course is full")
        public void testEnrollWhenFull() {
            // Fill the course
            for (int i = 0; i < 30; i++) {
                course.enrollStudent();
            }
            
            // Try to enroll one more
            boolean enrolled = course.enrollStudent();
            assertFalse(enrolled);
            assertEquals(30, course.getEnrolledStudents());
        }
        
        @Test
        @DisplayName("Should unenroll student successfully")
        public void testUnenrollStudent() {
            course.enrollStudent();
            assertEquals(1, course.getEnrolledStudents());
            
            boolean unenrolled = course.unenrollStudent();
            assertTrue(unenrolled);
            assertEquals(0, course.getEnrolledStudents());
        }
        
        @Test
        @DisplayName("Should not unenroll when no students enrolled")
        public void testUnenrollWhenEmpty() {
            boolean unenrolled = course.unenrollStudent();
            assertFalse(unenrolled);
            assertEquals(0, course.getEnrolledStudents());
        }
        
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20, 25, 29})
        @DisplayName("Should correctly report available seats")
        public void testAvailableSeats(int enrollments) {
            for (int i = 0; i < enrollments; i++) {
                course.enrollStudent();
            }
            assertEquals(30 - enrollments, course.getAvailableSeats());
        }
    }
    
    @Nested
    @DisplayName("Course Status Tests")
    class CourseStatusTests {
        
        @Test
        @DisplayName("Should report course not full initially")
        public void testNotFullInitially() {
            assertFalse(course.isFull());
        }
        
        @Test
        @DisplayName("Should report course full when at capacity")
        public void testFullWhenAtCapacity() {
            for (int i = 0; i < 30; i++) {
                course.enrollStudent();
            }
            assertCourseIsFull(course);
        }
        
        @ParameterizedTest
        @CsvSource({
            "0, false",
            "15, false",
            "29, false",
            "30, true"
        })
        @DisplayName("Should correctly report full status")
        public void testFullStatus(int enrollments, boolean expectedFull) {
            for (int i = 0; i < enrollments; i++) {
                course.enrollStudent();
            }
            assertEquals(expectedFull, course.isFull());
        }
    }
    
    @Nested
    @DisplayName("Course Information Tests")
    class CourseInformationTests {
        
        @Test
        @DisplayName("Should update instructor")
        public void testUpdateInstructor() {
            course.setInstructor("Dr. Johnson");
            assertEquals("Dr. Johnson", course.getInstructor());
        }
        
        @Test
        @DisplayName("Should generate correct toString")
        public void testToString() {
            course.enrollStudent();
            course.enrollStudent();
            String result = course.toString();
            
            assertTrue(result.contains("CS101"));
            assertTrue(result.contains("Introduction to Java"));
            assertTrue(result.contains("2/30"));
        }
        
        @Test
        @DisplayName("Should compare courses by ID")
        public void testEqualsAndHashCode() {
            Course course2 = new Course("CS101", "Different Name", 4, "Dr. Brown", 25);
            Course course3 = new Course("CS102", "Introduction to Java", 3, "Dr. Smith", 30);
            
            assertEquals(course, course2);
            assertNotEquals(course, course3);
            assertEquals(course.hashCode(), course2.hashCode());
        }
    }
    
    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Should handle single seat course")
        public void testSingleSeatCourse() {
            Course singleSeat = new Course("CS999", "Special Topic", 1, "Dr. X", 1);
            assertTrue(singleSeat.enrollStudent());
            assertCourseIsFull(singleSeat);
            assertFalse(singleSeat.enrollStudent());
        }
        
        @Test
        @DisplayName("Should handle large enrollment numbers")
        public void testLargeEnrollment() {
            Course largeCourse = new Course("CS500", "Popular Course", 3, "Dr. Popular", 500);
            
            for (int i = 0; i < 500; i++) {
                assertTrue(largeCourse.enrollStudent());
            }
            
            assertTrue(largeCourse.isFull());
            assertEquals(500, largeCourse.getEnrolledStudents());
            assertFalse(largeCourse.enrollStudent());
        }
        
        @Test
        @DisplayName("Should handle rapid enrollment and unenrollment")
        public void testRapidEnrollmentCycles() {
            for (int cycle = 0; cycle < 10; cycle++) {
                assertTrue(course.enrollStudent());
                assertTrue(course.unenrollStudent());
            }
            
            assertEquals(0, course.getEnrolledStudents());
            assertFalse(course.isFull());
        }
    }
}
