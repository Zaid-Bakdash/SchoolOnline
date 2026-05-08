package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Department;
import com.bootcamp.onlineschool.model.Teacher;
import com.bootcamp.onlineschool.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Department Service Tests")
public class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    public void setUp() {
        courseService.getAllCourses().forEach(c -> courseService.deleteCourse(c.getId()));
        teacherService.getAllTeachers().forEach(t -> teacherService.deleteTeacher(t.getId()));
        departmentRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create department successfully")
    public void testCreateDepartment() {
        Department department = new Department("Mathematics", "MATH", 110000.0, "Building A");
        Department created = departmentService.createDepartment(department);

        assertNotNull(created.getId());
        assertEquals("MATH", created.getCode());
    }

    @Test
    @DisplayName("Should get department by ID")
    public void testGetDepartmentById() {
        Department department = departmentService.createDepartment(new Department("Science", "SCI", 120000.0, "Building B"));
        Department found = departmentService.getDepartmentById(department.getId());

        assertNotNull(found);
        assertEquals("Science", found.getName());
    }

    @Test
    @DisplayName("Should update department")
    public void testUpdateDepartment() {
        Department department = departmentService.createDepartment(new Department("English", "ENG", 90000.0, "Building C"));
        Department update = new Department("English Language", "ENG", 95000.0, "Building C");

        Department result = departmentService.updateDepartment(department.getId(), update);

        assertEquals("English Language", result.getName());
        assertEquals(95000.0, result.getBudget());
    }

    @Test
    @DisplayName("Should delete department")
    public void testDeleteDepartment() {
        Department department = departmentService.createDepartment(new Department("History", "HIS", 75000.0, "Building D"));
        departmentService.deleteDepartment(department.getId());

        assertThrows(DepartmentService.DepartmentNotFoundException.class,
                () -> departmentService.getDepartmentById(department.getId()));
    }

    @Test
    @DisplayName("Should assign teacher to department")
    public void testAssignTeacherToDepartment() {
        Department department = departmentService.createDepartment(new Department("Mathematics", "MATH", 130000.0, "Building A"));
        Teacher teacher = teacherService.createTeacher(new Teacher("John Smith", "john@school.edu", null, 5, 85000.0));

        Department updated = departmentService.assignTeacherToDepartment(department.getId(), teacher.getId());

        assertEquals(1, updated.getTeacherCount());
        assertTrue(updated.getTeachers().stream().anyMatch(t -> t.getId().equals(teacher.getId())));
    }

    @Test
    @DisplayName("Should remove teacher from department")
    public void testRemoveTeacherFromDepartment() {
        Department department = departmentService.createDepartment(new Department("Science", "SCI", 140000.0, "Building B"));
        Teacher teacher = teacherService.createTeacher(new Teacher("Jane Doe", "jane@school.edu", null, 4, 78000.0));
        departmentService.assignTeacherToDepartment(department.getId(), teacher.getId());

        Department updated = departmentService.removeTeacherFromDepartment(department.getId(), teacher.getId());

        assertEquals(0, updated.getTeacherCount());
        assertNull(teacherService.getTeacherById(teacher.getId()).getDepartment());
    }

    @Test
    @DisplayName("Should assign course to department")
    public void testAssignCourseToDepartment() {
        Department department = departmentService.createDepartment(new Department("Biology", "BIO", 125000.0, "Building C"));
        Course course = courseService.createCourse(new Course("Biology 101", "Intro to Biology", 4, 40));

        Department updated = departmentService.assignCourseToDepartment(department.getId(), course.getId());

        assertEquals(1, updated.getCourseCount());
        assertTrue(updated.getCourses().stream().anyMatch(c -> c.getId().equals(course.getId())));
    }

    @Test
    @DisplayName("Should remove course from department")
    public void testRemoveCourseFromDepartment() {
        Department department = departmentService.createDepartment(new Department("Physics", "PHY", 135000.0, "Building D"));
        Course course = courseService.createCourse(new Course("Physics 101", "Intro to Physics", 4, 40));
        departmentService.assignCourseToDepartment(department.getId(), course.getId());

        Department updated = departmentService.removeCourseFromDepartment(department.getId(), course.getId());

        assertEquals(0, updated.getCourseCount());
        assertNull(courseService.getCourseById(course.getId()).getDepartment());
    }

    @Test
    @DisplayName("Should get department with teachers")
    public void testGetDepartmentWithTeachers() {
        Department department = departmentService.createDepartment(new Department("Chemistry", "CHE", 150000.0, "Building E"));
        Teacher teacher = teacherService.createTeacher(new Teacher("Mary Jane", "mary@school.edu", null, 6, 89000.0));
        departmentService.assignTeacherToDepartment(department.getId(), teacher.getId());

        Department loaded = departmentService.getDepartmentWithTeachers(department.getId());
        assertEquals(1, loaded.getTeachers().size());
    }

    @Test
    @DisplayName("Should get department with courses")
    public void testGetDepartmentWithCourses() {
        Department department = departmentService.createDepartment(new Department("English", "ENG", 120000.0, "Building F"));
        Course course = courseService.createCourse(new Course("Composition", "Writing practice", 3, 30));
        departmentService.assignCourseToDepartment(department.getId(), course.getId());

        Department loaded = departmentService.getDepartmentWithCourses(department.getId());
        assertEquals(1, loaded.getCourses().size());
    }
}
