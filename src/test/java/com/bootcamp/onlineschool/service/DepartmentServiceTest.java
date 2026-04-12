package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DepartmentService tests demonstrating Spring Boot testing
 */
@SpringBootTest
@DisplayName("DepartmentService Tests")
public class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @BeforeEach
    public void setUp() {
        // Clear all departments before each test
        departmentService.clear();
    }

    @Test
    @DisplayName("Should create department successfully")
    public void testCreateDepartment() {
        Department dept = departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 100000.0);

        assertNotNull(dept);
        assertEquals("CS", dept.getId());
        assertEquals("Computer Science", dept.getName());
        assertEquals("Dr. Smith", dept.getHead());
        assertEquals(100000.0, dept.getBudget());
        assertEquals(0, dept.getTeacherCount());
    }

    @Test
    @DisplayName("Should throw exception when creating department with existing ID")
    public void testCreateDepartmentWithExistingId() {
        departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 100000.0);

        assertThrows(DepartmentService.DepartmentAlreadyExistsException.class,
            () -> departmentService.createDepartment("CS", "Computer Science 2", "Dr. Jones", 50000.0));
    }

    @Test
    @DisplayName("Should get department by ID")
    public void testGetDepartmentById() {
        departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 100000.0);

        Department dept = departmentService.getDepartmentById("CS");
        assertNotNull(dept);
        assertEquals("Computer Science", dept.getName());
    }

    @Test
    @DisplayName("Should throw exception when department not found")
    public void testGetNonExistentDepartment() {
        assertThrows(DepartmentService.DepartmentNotFoundException.class,
            () -> departmentService.getDepartmentById("NONEXISTENT"));
    }

    @Test
    @DisplayName("Should get all departments")
    public void testGetAllDepartments() {
        departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 100000.0);
        departmentService.createDepartment("MATH", "Mathematics", "Dr. Jones", 80000.0);

        List<Department> departments = departmentService.getAllDepartments();
        assertEquals(2, departments.size());
    }

    @Test
    @DisplayName("Should assign teacher to department")
    public void testAssignTeacherToDepartment() {
        departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 100000.0);

        departmentService.assignTeacherToDepartment("CS", "TCH001");

        Department dept = departmentService.getDepartmentById("CS");
        assertEquals(1, dept.getTeacherCount());
        assertTrue(dept.getTeacherIds().contains("TCH001"));
    }

    @Test
    @DisplayName("Should throw exception when assigning teacher to non-existent department")
    public void testAssignTeacherToNonExistentDepartment() {
        assertThrows(DepartmentService.DepartmentNotFoundException.class,
            () -> departmentService.assignTeacherToDepartment("NONEXISTENT", "TCH001"));
    }

    @Test
    @DisplayName("Should remove teacher from department")
    public void testRemoveTeacherFromDepartment() {
        departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 100000.0);
        departmentService.assignTeacherToDepartment("CS", "TCH001");
        departmentService.assignTeacherToDepartment("CS", "TCH002");

        departmentService.removeTeacherFromDepartment("CS", "TCH001");

        Department dept = departmentService.getDepartmentById("CS");
        assertEquals(1, dept.getTeacherCount());
        assertFalse(dept.getTeacherIds().contains("TCH001"));
        assertTrue(dept.getTeacherIds().contains("TCH002"));
    }

    @Test
    @DisplayName("Should get departments by budget range")
    public void testGetDepartmentsByBudgetRange() {
        departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 100000.0);
        departmentService.createDepartment("MATH", "Mathematics", "Dr. Jones", 80000.0);
        departmentService.createDepartment("PHYS", "Physics", "Dr. Brown", 120000.0);

        List<Department> midRange = departmentService.getDepartmentsByBudgetRange(90000.0, 110000.0);
        assertEquals(1, midRange.size());
        assertEquals("CS", midRange.get(0).getId());
    }

    @Test
    @DisplayName("Should get total budget")
    public void testGetTotalBudget() {
        departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 100000.0);
        departmentService.createDepartment("MATH", "Mathematics", "Dr. Jones", 80000.0);

        double totalBudget = departmentService.getTotalBudget();
        assertEquals(180000.0, totalBudget);
    }

    @Test
    @DisplayName("Should delete department")
    public void testDeleteDepartment() {
        departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 100000.0);
        assertEquals(1, departmentService.getDepartmentCount());

        departmentService.deleteDepartment("CS");
        assertEquals(0, departmentService.getDepartmentCount());
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent department")
    public void testDeleteNonExistentDepartment() {
        assertThrows(DepartmentService.DepartmentNotFoundException.class,
            () -> departmentService.deleteDepartment("NONEXISTENT"));
    }

    @Test
    @DisplayName("Should handle empty department list")
    public void testEmptyDepartmentList() {
        List<Department> departments = departmentService.getAllDepartments();
        assertTrue(departments.isEmpty());

        double totalBudget = departmentService.getTotalBudget();
        assertEquals(0.0, totalBudget);
    }

    @Test
    @DisplayName("Should handle duplicate teacher assignment")
    public void testDuplicateTeacherAssignment() {
        departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 100000.0);

        departmentService.assignTeacherToDepartment("CS", "TCH001");
        departmentService.assignTeacherToDepartment("CS", "TCH001"); // Duplicate

        Department dept = departmentService.getDepartmentById("CS");
        assertEquals(1, dept.getTeacherCount()); // Should still be 1
    }
}