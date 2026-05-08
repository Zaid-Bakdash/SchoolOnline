package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Department;
import com.bootcamp.onlineschool.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Department Repository Tests")
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Department mathDepartment;
    private Department scienceDepartment;

    @BeforeEach
    public void setUp() {
        mathDepartment = departmentRepository.save(new Department("Mathematics", "MATH", 100000.0, "Building A"));
        scienceDepartment = departmentRepository.save(new Department("Science", "SCI", 120000.0, "Building B"));

        teacherRepository.save(new Teacher("John Smith", "john@school.edu", mathDepartment, 5, 85000.0));
        teacherRepository.save(new Teacher("Bob Johnson", "bob@school.edu", mathDepartment, 8, 95000.0));
        teacherRepository.save(new Teacher("Jane Doe", "jane@school.edu", scienceDepartment, 3, 75000.0));

        Course course1 = new Course("Algebra", "Algebra Basics", 3, 30);
        course1.setDepartment(mathDepartment);
        courseRepository.save(course1);

        Course course2 = new Course("Biology", "Biology 101", 4, 40);
        course2.setDepartment(scienceDepartment);
        courseRepository.save(course2);
    }

    @Test
    @DisplayName("Should find department by code")
    public void testFindByCode() {
        Optional<Department> found = departmentRepository.findByCode("MATH");
        assertTrue(found.isPresent());
        assertEquals("Mathematics", found.get().getName());
    }

    @Test
    @DisplayName("Should find department by name")
    public void testFindByName() {
        Optional<Department> found = departmentRepository.findByName("Science");
        assertTrue(found.isPresent());
        assertEquals("SCI", found.get().getCode());
    }

    @Test
    @DisplayName("Should find departments with budget greater than threshold")
    public void testFindByBudgetGreaterThan() {
        List<Department> departments = departmentRepository.findByBudgetGreaterThan(110000.0);
        assertEquals(1, departments.size());
        assertEquals("Science", departments.get(0).getName());
    }

    @Test
    @DisplayName("Should load department with teachers")
    public void testFindByIdWithTeachers() {
        Optional<Department> found = departmentRepository.findByIdWithTeachers(mathDepartment.getId());
        assertTrue(found.isPresent());
        assertEquals(2, found.get().getTeachers().size());
    }

    @Test
    @DisplayName("Should load department with courses")
    public void testFindByIdWithCourses() {
        Optional<Department> found = departmentRepository.findByIdWithCourses(scienceDepartment.getId());
        assertTrue(found.isPresent());
        assertEquals(1, found.get().getCourses().size());
    }

    @Test
    @DisplayName("Should count teachers by department")
    public void testCountTeachersByDepartment() {
        long count = departmentRepository.countTeachersByDepartment(mathDepartment);
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should calculate total budget across departments")
    public void testGetTotalBudget() {
        Double total = departmentRepository.getTotalBudget();
        assertNotNull(total);
        assertEquals(220000.0, total);
    }

    @Test
    @DisplayName("Should save and retrieve department")
    public void testSaveAndRetrieveDepartment() {
        Department newDepartment = new Department("English", "ENG", 80000.0, "Building C");
        Department saved = departmentRepository.save(newDepartment);

        assertNotNull(saved.getId());
        Optional<Department> retrieved = departmentRepository.findById(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("English", retrieved.get().getName());
    }

    @Test
    @DisplayName("Should update department budget")
    public void testUpdateDepartment() {
        Department existing = departmentRepository.findById(mathDepartment.getId()).orElseThrow();
        existing.setBudget(105000.0);
        Department updated = departmentRepository.save(existing);

        assertEquals(105000.0, updated.getBudget());
    }

    @Test
    @DisplayName("Should delete department")
    public void testDeleteDepartment() {
        departmentRepository.deleteById(scienceDepartment.getId());
        assertFalse(departmentRepository.findById(scienceDepartment.getId()).isPresent());
    }

    @Test
    @DisplayName("Should return empty optional for missing department code")
    public void testFindByCodeNotFound() {
        Optional<Department> missing = departmentRepository.findByCode("XYZ");
        assertTrue(missing.isEmpty());
    }
}
