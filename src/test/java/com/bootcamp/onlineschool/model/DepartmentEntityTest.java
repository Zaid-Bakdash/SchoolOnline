package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Department Entity Tests")
public class DepartmentEntityTest {

    private Department department;
    private Teacher teacher;
    private Course course;

    @BeforeEach
    public void setUp() {
        department = new Department("Mathematics", "MATH", 100000.0, "Building A");
        teacher = new Teacher("John Smith", "john@school.edu", department, 5, 85000.0);
        course = new Course("Algebra", "Introductory Algebra", 3, 30);
    }

    @Test
    @DisplayName("Should create valid department")
    public void testCreateValidDepartment() {
        assertNotNull(department);
        assertEquals("Mathematics", department.getName());
        assertEquals("MATH", department.getCode());
        assertEquals(100000.0, department.getBudget());
        assertEquals("Building A", department.getLocation());
    }

    @Test
    @DisplayName("Should set and get all fields")
    public void testSettersAndGetters() {
        department.setName("Science");
        department.setCode("SCI");
        department.setBudget(150000.0);
        department.setLocation("Building B");

        assertEquals("Science", department.getName());
        assertEquals("SCI", department.getCode());
        assertEquals(150000.0, department.getBudget());
        assertEquals("Building B", department.getLocation());
    }

    @Test
    @DisplayName("Should add teacher to department")
    public void testAddTeacher() {
        department.addTeacher(teacher);

        assertEquals(1, department.getTeacherCount());
        assertTrue(department.getTeachers().contains(teacher));
        assertSame(department, teacher.getDepartment());
    }

    @Test
    @DisplayName("Should remove teacher from department")
    public void testRemoveTeacher() {
        department.addTeacher(teacher);
        department.removeTeacher(teacher);

        assertEquals(0, department.getTeacherCount());
        assertFalse(department.getTeachers().contains(teacher));
        assertNull(teacher.getDepartment());
    }

    @Test
    @DisplayName("Should add course to department")
    public void testAddCourse() {
        department.addCourse(course);

        assertEquals(1, department.getCourseCount());
        assertTrue(department.getCourses().contains(course));
        assertSame(department, course.getDepartment());
    }

    @Test
    @DisplayName("Should remove course from department")
    public void testRemoveCourse() {
        department.addCourse(course);
        department.removeCourse(course);

        assertEquals(0, department.getCourseCount());
        assertFalse(department.getCourses().contains(course));
        assertNull(course.getDepartment());
    }

    @Test
    @DisplayName("Should increase budget")
    public void testIncreaseBudget() {
        department.increaseBudget(5000.0);
        assertEquals(105000.0, department.getBudget());
    }

    @Test
    @DisplayName("Should decrease budget")
    public void testDecreaseBudget() {
        department.decreaseBudget(20000.0);
        assertEquals(80000.0, department.getBudget());
    }

    @Test
    @DisplayName("Should return correct counts")
    public void testGetCounts() {
        department.addTeacher(teacher);
        department.addCourse(course);

        assertEquals(1, department.getTeacherCount());
        assertEquals(1, department.getCourseCount());
    }

    @Test
    @DisplayName("Should use code for equals and hashCode")
    public void testEqualsAndHashCode() {
        Department other = new Department("Mathematics", "MATH", 90000.0, "Building C");
        assertEquals(department, other);
        assertEquals(department.hashCode(), other.hashCode());
    }
}
