package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Department;
import com.bootcamp.onlineschool.model.Teacher;
import com.bootcamp.onlineschool.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TeacherService tests
 * Tests service layer with @SpringBootTest for integration testing
 */
@SpringBootTest
@DisplayName("Teacher Service Tests")
public class TeacherServiceTest {
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @BeforeEach
    public void setUp() {
        // Clear all data before each test
        courseService.getAllCourses().forEach(c -> courseService.deleteCourse(c.getId()));
        teacherService.getAllTeachers().forEach(t -> teacherService.deleteTeacher(t.getId()));
        departmentRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Should create teacher successfully")
    public void testCreateTeacher() {
        Teacher teacher = new Teacher("John Smith", "john@school.edu", null, 5, 85000.0);
        Teacher created = teacherService.createTeacher(teacher);
        
        assertNotNull(created.getId());
        assertEquals("John Smith", created.getName());
        assertEquals("john@school.edu", created.getEmail());
    }
    
    @Test
    @DisplayName("Should throw exception when creating null teacher")
    public void testCreateNullTeacher() {
        assertThrows(IllegalArgumentException.class, () -> teacherService.createTeacher(null));
    }
    
    @Test
    @DisplayName("Should throw exception when creating duplicate teacher")
    public void testCreateDuplicateTeacher() {
        Teacher teacher = new Teacher("John Smith", "john@school.edu", null, 5, 85000.0);
        teacherService.createTeacher(teacher);
        
        Teacher duplicate = new Teacher("Different Name", "john@school.edu", null, 3, 75000.0);
        assertThrows(TeacherService.TeacherAlreadyExistsException.class, 
            () -> teacherService.createTeacher(duplicate));
    }
    
    @Test
    @DisplayName("Should get teacher by ID")
    public void testGetTeacherById() {
        Teacher teacher = new Teacher("John Smith", "john@school.edu", null, 5, 85000.0);
        Teacher created = teacherService.createTeacher(teacher);
        
        Teacher found = teacherService.getTeacherById(created.getId());
        assertNotNull(found);
        assertEquals("John Smith", found.getName());
    }
    
    @Test
    @DisplayName("Should throw exception when teacher not found")
    public void testGetTeacherNotFound() {
        assertThrows(TeacherService.TeacherNotFoundException.class, 
            () -> teacherService.getTeacherById(999L));
    }
    
    @Test
    @DisplayName("Should get all teachers")
    public void testGetAllTeachers() {
        teacherService.createTeacher(new Teacher("John Smith", "john@school.edu", null, 5, 85000.0));
        teacherService.createTeacher(new Teacher("Jane Doe", "jane@school.edu", null, 3, 75000.0));
        
        List<Teacher> teachers = teacherService.getAllTeachers();
        assertEquals(2, teachers.size());
    }
    
    @Test
    @DisplayName("Should assign course to teacher")
    public void testAssignCourseToTeacher() {
        Teacher teacher = teacherService.createTeacher(new Teacher("John Smith", "john@school.edu", null, 5, 85000.0));
        Course course = courseService.createCourse(new Course("Algebra", "Algebra Basics", 3, 30));
        
        teacherService.assignCourseToTeacher(teacher.getId(), course.getId());
        
        Teacher updated = teacherService.getTeacherWithCourses(teacher.getId());
        assertEquals(1, updated.getCourses().size());
        assertTrue(updated.getCourses().stream().anyMatch(c -> c.getId().equals(course.getId())));
    }
    
    @Test
    @DisplayName("Should remove course from teacher")
    public void testRemoveCourseFromTeacher() {
        Teacher teacher = teacherService.createTeacher(new Teacher("John Smith", "john@school.edu", null, 5, 85000.0));
        Course course = courseService.createCourse(new Course("Algebra", "Algebra Basics", 3, 30));
        
        teacherService.assignCourseToTeacher(teacher.getId(), course.getId());
        teacherService.removeCourseFromTeacher(teacher.getId(), course.getId());
        
        Teacher updated = teacherService.getTeacherWithCourses(teacher.getId());
        assertEquals(0, updated.getCourses().size());
    }
    
    @Test
    @DisplayName("Should update teacher")
    public void testUpdateTeacher() {
        Teacher teacher = teacherService.createTeacher(new Teacher("John Smith", "john@school.edu", null, 5, 85000.0));
        
        Teacher updated = new Teacher("John Smith Updated", "john@school.edu", null, 6, 90000.0);
        Teacher result = teacherService.updateTeacher(teacher.getId(), updated);
        
        assertEquals(6, result.getYearsOfExperience());
        assertEquals(90000.0, result.getSalary());
    }
    
    @Test
    @DisplayName("Should delete teacher")
    public void testDeleteTeacher() {
        Teacher teacher = teacherService.createTeacher(new Teacher("John Smith", "john@school.edu", null, 5, 85000.0));
        Long id = teacher.getId();
        
        teacherService.deleteTeacher(id);
        
        assertThrows(TeacherService.TeacherNotFoundException.class, 
            () -> teacherService.getTeacherById(id));
    }
}
