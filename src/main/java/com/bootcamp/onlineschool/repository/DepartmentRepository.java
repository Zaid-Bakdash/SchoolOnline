package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.model.Department;
import com.bootcamp.onlineschool.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DepartmentRepository - JPA Repository for Department entities
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByCode(String code);

    Optional<Department> findByName(String name);

    List<Department> findByBudgetGreaterThan(Double budget);

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.teachers WHERE d.id = :id")
    Optional<Department> findByIdWithTeachers(@Param("id") Long id);

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.courses WHERE d.id = :id")
    Optional<Department> findByIdWithCourses(@Param("id") Long id);

    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.department = :department")
    long countTeachersByDepartment(@Param("department") Department department);

    @Query("SELECT SUM(d.budget) FROM Department d")
    Double getTotalBudget();
}
