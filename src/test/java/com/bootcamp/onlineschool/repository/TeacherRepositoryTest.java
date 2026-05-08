package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Department;
import com.bootcamp.onlineschool.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TeacherRepository tests
 * Tests repository methods and custom queries
 */
@DataJpaTest
@DisplayName("Teacher Repository Tests")
public class TeacherRepositoryTest {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    private Department mathDepartment;
    private Department scienceDepartment;
    private Teacher teacher1;
    private Teacher teacher2;
    private Teacher teacher3;
    
    @BeforeEach
    public void setUp() {
        mathDepartment = departmentRepository.save(new Department("Mathematics", "MATH", 100000.0, "Building A"));
        scienceDepartment = departmentRepository.save(new Department("Science", "SCI", 120000.0, "Building B"));

        teacher1 = new Teacher("John Smith", "john@school.edu", mathDepartment, 5, 85000.0);
        teacher2 = new Teacher("Jane Doe", "jane@school.edu", scienceDepartment, 3, 75000.0);
        teacher3 = new Teacher("Bob Johnson", "bob@school.edu", mathDepartment, 8, 95000.0);
        
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);
    }
    
    @Test
    @DisplayName("Should find teacher by email")
    public void testFindByEmail() {
        Optional<Teacher> found = teacherRepository.findByEmail("john@school.edu");
        assertTrue(found.isPresent());
        assertEquals("John Smith", found.get().getName());
    }
    
    @Test
    @DisplayName("Should not find teacher with non-existent email")
    public void testFindByEmailNotFound() {
        Optional<Teacher> found = teacherRepository.findByEmail("nonexistent@school.edu");
        assertFalse(found.isPresent());
    }
    
    @Test
    @DisplayName("Should find all teachers in department")
    public void testFindByDepartment() {
        List<Teacher> mathTeachers = teacherRepository.findByDepartment("Mathematics");
        assertEquals(2, mathTeachers.size());
        assertTrue(mathTeachers.stream().allMatch(t -> t.getDepartment() != null && t.getDepartment().getName().equals("Mathematics")));
    }
    
    @Test
    @DisplayName("Should find teachers with minimum experience")
    public void testFindByYearsOfExperienceGreaterThanEqual() {
        List<Teacher> experienced = teacherRepository.findByYearsOfExperienceGreaterThanEqual(5);
        assertEquals(2, experienced.size());
        assertTrue(experienced.stream().allMatch(t -> t.getYearsOfExperience() >= 5));
    }
    
    @Test
    @DisplayName("Should find teachers with salary in range")
    public void testFindBySalaryBetween() {
        List<Teacher> teachers = teacherRepository.findBySalaryBetween(74000.0, 85000.0);
        assertEquals(2, teachers.size());
    }
    
    @Test
    @DisplayName("Should find teacher by ID with courses")
    public void testFindByIdWithCourses() {
        Teacher saved = teacherRepository.save(new Teacher("John Smith", "john.courses@school.edu", null, 5, 85000.0));
        Optional<Teacher> found = teacherRepository.findByIdWithCourses(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }
    
    @Test
    @DisplayName("Should find teachers with minimum courses")
    public void testFindTeachersWithMinCourses() {
        Teacher saved = teacherRepository.save(teacher1);
        Course course1 = new Course("Java", "Java Programming", 3, 32);
        Course course2 = new Course("Python", "Python Basics", 3, 30);
        course1.setTeacher(saved);
        course2.setTeacher(saved);
        courseRepository.save(course1);
        courseRepository.save(course2);
        
        List<Teacher> teachers = teacherRepository.findTeachersWithMinCourses(2);
        assertTrue(teachers.stream().anyMatch(t -> t.getId().equals(saved.getId())));
    }
    
    @Test
    @DisplayName("Should get average salary by department")
    public void testGetAverageSalaryByDepartment() {
        Double avgSalary = teacherRepository.getAverageSalaryByDepartment("Mathematics");
        assertNotNull(avgSalary);
        assertEquals((85000.0 + 95000.0) / 2, avgSalary, 0.01);
    }
    
    @Test
    @DisplayName("Should save and retrieve teacher")
    public void testSaveAndRetrieve() {
        Teacher newTeacher = new Teacher("Alice Brown", "alice@school.edu", null, 2, 65000.0);
        Teacher saved = teacherRepository.save(newTeacher);
        
        assertNotNull(saved.getId());
        Optional<Teacher> retrieved = teacherRepository.findById(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Alice Brown", retrieved.get().getName());
    }
    
    @Test
    @DisplayName("Should update teacher")
    public void testUpdateTeacher() {
        Teacher saved = teacherRepository.save(teacher1);
        saved.setSalary(90000.0);
        Teacher updated = teacherRepository.save(saved);
        
        assertEquals(90000.0, updated.getSalary());
    }
    
    @Test
    @DisplayName("Should delete teacher")
    public void testDeleteTeacher() {
        Teacher saved = teacherRepository.save(teacher1);
        teacherRepository.delete(saved);
        
        Optional<Teacher> retrieved = teacherRepository.findById(saved.getId());
        assertFalse(retrieved.isPresent());
    }

    @Test
    @DisplayName("Should return empty optional for not found by email")
    public void testFindByEmailEmpty() {
        Optional<Teacher> notFound = teacherRepository.findByEmail("notexist@school.edu");
        assertTrue(notFound.isEmpty());
    }
}
