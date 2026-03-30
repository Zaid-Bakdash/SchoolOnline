package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Course class
 * Demonstrates:
 * - Testing object creation
 * - Testing getters and setters
 * - Testing toString() method
 * - Testing equals() and hashCode() methods
 */
@DisplayName("Course Class Tests")
public class CourseTest {
    
    private Course course1;
    private Course course2;
    private Course course3;
    
    @BeforeEach
    public void setUp() {
        course1 = new Course("CS101", "Introduction to Java", 3);
        course2 = new Course("CS102", "Data Structures", 4);
        course3 = new Course("CS101", "Intro to Java", 3);
    }
    
    @Test
    @DisplayName("Should create course with all fields")
    public void testCourseCreation() {
        assertNotNull(course1);
        assertEquals("CS101", course1.getCourseId());
        assertEquals("Introduction to Java", course1.getName());
        assertEquals(3, course1.getCredits());
    }
    
    @Test
    @DisplayName("Should get and set course ID")
    public void testGetSetCourseId() {
        course1.setCourseId("CS999");
        assertEquals("CS999", course1.getCourseId());
    }
    
    @Test
    @DisplayName("Should get and set course name")
    public void testGetSetName() {
        course1.setName("Advanced Java Programming");
        assertEquals("Advanced Java Programming", course1.getName());
    }
    
    @Test
    @DisplayName("Should get and set credits")
    public void testGetSetCredits() {
        course1.setCredits(4);
        assertEquals(4, course1.getCredits());
    }
    
    @Test
    @DisplayName("Should return correct toString representation")
    public void testToString() {
        String expected = "Course{id='CS101', name='Introduction to Java', credits=3}";
        assertEquals(expected, course1.toString());
    }
    
    @Test
    @DisplayName("Should determine equality based on courseId")
    public void testEquals() {
        // same courseId should be equal
        Course course1Copy = new Course("CS101", "Different Name", 4);
        assertEquals(course1, course1Copy);
        
        // different courseId should not be equal
        assertNotEquals(course1, course2);
    }
    
    @Test
    @DisplayName("Should return same hashCode for equal courses")
    public void testHashCode() {
        Course course1Copy = new Course("CS101", "Intro Java", 3);
        assertEquals(course1.hashCode(), course1Copy.hashCode());
        
        assertNotEquals(course1.hashCode(), course2.hashCode());
    }
    
    @Test
    @DisplayName("Should handle equals with null")
    public void testEqualsWithNull() {
        assertNotEquals(course1, null);
    }
    
    @Test
    @DisplayName("Should handle equals with different type")
    public void testEqualsWithDifferentType() {
        assertNotEquals(course1, "CS101");
    }
    
    @Test
    @DisplayName("Should use courseId for hashCode")
    public void testHashCodeConsistency() {
        int hash1 = course1.hashCode();
        int hash2 = course1.hashCode();
        assertEquals(hash1, hash2);
    }
}
