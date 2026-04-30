package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("DepartmentRepository Tests")
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department csDepartment;
    private Department mathDepartment;
    private Department physicsDepartment;
    private Department artDepartment;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();

        csDepartment = new Department("D001", "Computer Science", "Dr. Ada Lovelace", 200000.0, "Building A", LocalDate.of(2018, 1, 10));
        mathDepartment = new Department("D002", "Mathematics", "Dr. Euler", 150000.0, "Building B", LocalDate.of(2020, 5, 20));
        physicsDepartment = new Department("D003", "Physics", "Dr. Newton", 180000.0, "Building A", LocalDate.of(2019, 9, 15));
        artDepartment = new Department("D004", "Art", "Prof. Picasso", 120000.0, "Building C", LocalDate.of(2021, 2, 1));

        departmentRepository.save(csDepartment);
        departmentRepository.save(mathDepartment);
        departmentRepository.save(physicsDepartment);
        departmentRepository.save(artDepartment);
    }

    @Test
    @DisplayName("Should save and retrieve department")
    void testSaveAndRetrieve() {
        Department saved = departmentRepository.save(new Department("D005", "History", "Dr. Herodotus", 130000.0, "Building D", LocalDate.of(2022, 3, 12)));

        assertNotNull(saved.getId());
        assertEquals("History", saved.getName());
    }

    @Test
    @DisplayName("Should find department by department ID")
    void testFindByDepartmentId() {
        Department found = departmentRepository.findByDepartmentId("D001");

        assertNotNull(found);
        assertEquals("Computer Science", found.getName());
    }

    @Test
    @DisplayName("Should find department by name")
    void testFindByName() {
        Department found = departmentRepository.findByName("Mathematics");

        assertNotNull(found);
        assertEquals("Dr. Euler", found.getHead());
    }

    @Test
    @DisplayName("Should find departments with budget greater than threshold")
    void testFindByBudgetGreaterThan() {
        List<Department> results = departmentRepository.findByBudgetGreaterThan(170000.0);

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(department -> "Computer Science".equals(department.getName())));
        assertTrue(results.stream().anyMatch(department -> "Physics".equals(department.getName())));
    }

    @Test
    @DisplayName("Should find departments within budget range")
    void testFindByBudgetBetween() {
        List<Department> results = departmentRepository.findByBudgetBetween(130000.0, 190000.0);

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(department -> "Mathematics".equals(department.getName())));
        assertTrue(results.stream().anyMatch(department -> "Physics".equals(department.getName())));
    }

    @Test
    @DisplayName("Should find recently established departments")
    void testFindByEstablishedDateAfter() {
        List<Department> results = departmentRepository.findByEstablishedDateAfter(LocalDate.of(2019, 1, 1));

        assertEquals(3, results.size());
        assertTrue(results.stream().anyMatch(department -> "Mathematics".equals(department.getName())));
        assertTrue(results.stream().anyMatch(department -> "Art".equals(department.getName())));
    }

    @Test
    @DisplayName("Should sort departments by budget descending")
    void testFindAllSortedByBudgetDesc() {
        List<Department> results = departmentRepository.findAllSortedByBudgetDesc();

        assertEquals(4, results.size());
        assertEquals("Computer Science", results.get(0).getName());
        assertEquals("Physics", results.get(1).getName());
    }

    @Test
    @DisplayName("Should calculate total budget")
    void testGetTotalBudget() {
        Double total = departmentRepository.getTotalBudget();

        assertEquals(650000.0, total);
    }

    @Test
    @DisplayName("Should find departments with above-average budget")
    void testFindDepartmentsWithBudgetAboveAverage() {
        List<Department> results = departmentRepository.findDepartmentsWithBudgetAboveAverage();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(department -> "Computer Science".equals(department.getName())));
        assertTrue(results.stream().anyMatch(department -> "Physics".equals(department.getName())));
    }

    @Test
    @DisplayName("Should count departments by location")
    void testCountDepartmentsByLocation() {
        Long count = departmentRepository.countDepartmentsByLocation("Building A");

        assertEquals(2L, count);
    }

    @Test
    @DisplayName("Should find departments established in a specific year")
    void testFindDepartmentsEstablishedInYear() {
        List<Department> results = departmentRepository.findDepartmentsEstablishedInYear(2019);

        assertEquals(1, results.size());
        assertEquals("Physics", results.get(0).getName());
    }

    @Test
    @DisplayName("Should validate business methods and budget operations")
    void testBudgetBusinessMethods() {
        Department department = departmentRepository.findByDepartmentId("D002");

        assertTrue(department.isWithinBudget(100000.0));
        assertFalse(department.isWithinBudget(200000.0));

        department.increaseBudget(10000.0);
        assertEquals(160000.0, department.getBudget());

        department.decreaseBudget(5000.0);
        assertEquals(155000.0, department.getBudget());
    }

    @Test
    @DisplayName("Should delete department")
    void testDeleteDepartment() {
        Department found = departmentRepository.findByDepartmentId("D001");
        assertNotNull(found);

        departmentRepository.delete(found);
        assertFalse(departmentRepository.findById(found.getId()).isPresent());
    }
}
