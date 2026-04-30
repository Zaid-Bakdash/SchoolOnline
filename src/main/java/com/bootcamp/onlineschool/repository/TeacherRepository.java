package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByTeacherId(String teacherId);

    Teacher findByEmail(String email);

    List<Teacher> findByDepartment(String department);

    List<Teacher> findByYearsOfExperienceGreaterThanEqual(Integer yearsOfExperience);

    List<Teacher> findBySalaryBetween(Double minSalary, Double maxSalary);

    @Query("SELECT t FROM Teacher t ORDER BY t.name ASC")
    List<Teacher> findAllTeachersSortedByName();

    @Query("SELECT t FROM Teacher t WHERE t.hireDate > :hireDate")
    List<Teacher> findTeachersHiredAfter(@Param("hireDate") LocalDate hireDate);

    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.department = :department")
    Long countTeachersByDepartment(@Param("department") String department);

    @Query("SELECT AVG(t.salary) FROM Teacher t WHERE t.department = :department")
    Double calculateAverageSalaryByDepartment(@Param("department") String department);

    @Query("SELECT t FROM Teacher t ORDER BY t.salary DESC")
    List<Teacher> findTopNHighestPaidTeachers();
}