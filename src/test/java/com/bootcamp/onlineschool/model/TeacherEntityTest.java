package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TeacherEntity tests
 * Tests entity creation, validation, and relationships
 */
@DisplayName("Teacher Entity Tests")
public class TeacherEntityTest {
    
    private Teacher teacher;
    private Department department;

    @BeforeEach
    public void setUp() {
        department = new Department("Mathematics", "MATH", 100000.0, "Building A");
        teacher = new Teacher("John Smith", "john@school.edu", department, 5, 85000.0);
    }
    
    @Test
    @DisplayName("Should create valid teacher")
    public void testCreateValidTeacher() {
        assertNotNull(teacher);
        assertEquals("John Smith", teacher.getName());
        assertEquals("john@school.edu", teacher.getEmail());
        assertNotNull(teacher.getDepartment());
        assertEquals("Mathematics", teacher.getDepartment().getName());
        assertEquals(5, teacher.getYearsOfExperience());
        assertEquals(85000.0, teacher.getSalary());
    }
    
    @Test
    @DisplayName("Should set and get all fields")
    public void testSettersAndGetters() {
        teacher.setName("Jane Smith");
        teacher.setEmail("jane@school.edu");
        Department science = new Department("Science", "SCI", 120000.0, "Building B");
        teacher.setDepartment(science);
        teacher.setYearsOfExperience(10);
        teacher.setSalary(95000.0);
        
        assertEquals("Jane Smith", teacher.getName());
        assertEquals("jane@school.edu", teacher.getEmail());
        assertEquals("Science", teacher.getDepartment().getName());
        assertEquals(10, teacher.getYearsOfExperience());
        assertEquals(95000.0, teacher.getSalary());
    }
    
    @Test
    @DisplayName("Should add course to teacher")
    public void testAddCourse() {
        Course course = new Course("Java Basics", "Introduction to Java", 3, 30);
        teacher.addCourse(course);
        
        assertEquals(1, teacher.getCourses().size());
        assertTrue(teacher.getCourses().contains(course));
        assertEquals(teacher, course.getTeacher());
    }
    
    @Test
    @DisplayName("Should remove course from teacher")
    public void testRemoveCourse() {
        Course course = new Course("Java Basics", "Introduction to Java", 3, 30);
        teacher.addCourse(course);
        assertEquals(1, teacher.getCourses().size());
        
        teacher.removeCourse(course);
        assertEquals(0, teacher.getCourses().size());
        assertNull(course.getTeacher());
    }
    
    @Test
    @DisplayName("Should add multiple courses to teacher")
    public void testAddMultipleCourses() {
        Course course1 = new Course("Java Basics", "Introduction to Java", 3, 30);
        Course course2 = new Course("Advanced Java", "Advanced Topics", 4, 35);
        
        teacher.addCourse(course1);
        teacher.addCourse(course2);
        
        assertEquals(2, teacher.getCourses().size());
        assertTrue(teacher.getCourses().contains(course1));
        assertTrue(teacher.getCourses().contains(course2));
    }
    
    @Test
    @DisplayName("Should validate email uniqueness in equals")
    public void testEqualsBasedOnEmail() {
        Teacher other = new Teacher("Jane Doe", "john@school.edu", null, 3, 75000.0);
        assertEquals(teacher, other);
    }
    
    @Test
    @DisplayName("Should have different hashCode for different emails")
    public void testHashCodeBasedOnEmail() {
        Teacher other = new Teacher("Jane Doe", "jane@school.edu", null, 3, 75000.0);
        assertNotEquals(teacher.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Should initialize courses as empty set")
    public void testCoursesInitializedAsEmptySet() {
        Teacher newTeacher = new Teacher("Test Teacher", "test@school.edu", null, 4, 80000.0);
        assertNotNull(newTeacher.getCourses());
        assertEquals(0, newTeacher.getCourses().size());
    }
}
