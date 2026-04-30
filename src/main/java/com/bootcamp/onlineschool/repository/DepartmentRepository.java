package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department findByDepartmentId(String departmentId);

    Department findByName(String name);

    List<Department> findByBudgetGreaterThan(Double budget);

    List<Department> findByBudgetBetween(Double minBudget, Double maxBudget);

    List<Department> findByEstablishedDateAfter(LocalDate establishedDate);

    @Query("SELECT d FROM Department d ORDER BY d.budget DESC")
    List<Department> findAllSortedByBudgetDesc();

    @Query("SELECT SUM(d.budget) FROM Department d")
    Double getTotalBudget();

    @Query("SELECT d FROM Department d WHERE d.budget > (SELECT AVG(x.budget) FROM Department x)")
    List<Department> findDepartmentsWithBudgetAboveAverage();

    @Query("SELECT COUNT(d) FROM Department d WHERE d.location = :location")
    Long countDepartmentsByLocation(@Param("location") String location);

    @Query("SELECT d FROM Department d WHERE FUNCTION('YEAR', d.establishedDate) = :year")
    List<Department> findDepartmentsEstablishedInYear(@Param("year") Integer year);
}
