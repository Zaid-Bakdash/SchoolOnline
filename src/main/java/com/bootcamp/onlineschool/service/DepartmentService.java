package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Department;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DepartmentService manages departments and their associated teachers.
 * Demonstrates Spring Boot service layer with in-memory storage.
 */
@Service
public class DepartmentService {

    private final Map<String, Department> departments;

    public DepartmentService() {
        this.departments = new HashMap<>();
    }

    /**
     * Create a new department
     */
    public Department createDepartment(String id, String name, String head, double budget) {
        if (departments.containsKey(id)) {
            throw new DepartmentAlreadyExistsException("Department with ID " + id + " already exists");
        }

        Department department = new Department(id, name, head, budget);
        departments.put(id, department);
        return department;
    }

    /**
     * Get department by ID
     */
    public Department getDepartmentById(String id) {
        Department department = departments.get(id);
        if (department == null) {
            throw new DepartmentNotFoundException("Department not found: " + id);
        }
        return department;
    }

    /**
     * Get all departments
     */
    public List<Department> getAllDepartments() {
        return new ArrayList<>(departments.values());
    }

    /**
     * Assign teacher to department
     */
    public void assignTeacherToDepartment(String deptId, String teacherId) {
        Department department = getDepartmentById(deptId);
        department.addTeacherId(teacherId);
    }

    /**
     * Remove teacher from department
     */
    public void removeTeacherFromDepartment(String deptId, String teacherId) {
        Department department = getDepartmentById(deptId);
        department.removeTeacherId(teacherId);
    }

    /**
     * Get departments by budget range
     */
    public List<Department> getDepartmentsByBudgetRange(double min, double max) {
        return departments.values().stream()
                .filter(dept -> dept.getBudget() >= min && dept.getBudget() <= max)
                .collect(Collectors.toList());
    }

    /**
     * Get total budget across all departments
     */
    public double getTotalBudget() {
        return departments.values().stream()
                .mapToDouble(Department::getBudget)
                .sum();
    }

    /**
     * Delete department
     */
    public void deleteDepartment(String id) {
        if (!departments.containsKey(id)) {
            throw new DepartmentNotFoundException("Department not found: " + id);
        }
        departments.remove(id);
    }

    /**
     * Get department count
     */
    public int getDepartmentCount() {
        return departments.size();
    }

    /**
     * Clear all departments (for testing)
     */
    public void clear() {
        departments.clear();
    }

    /**
     * Custom exception for department not found
     */
    public static class DepartmentNotFoundException extends RuntimeException {
        public DepartmentNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Custom exception for department already exists
     */
    public static class DepartmentAlreadyExistsException extends RuntimeException {
        public DepartmentAlreadyExistsException(String message) {
            super(message);
        }
    }
}