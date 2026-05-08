package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TeacherRepository - JPA Repository for Teacher entities
 * Provides data access methods with custom query support
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    /**
     * Find teacher by email
     */
    Optional<Teacher> findByEmail(String email);
    
    /**
     * Find all teachers in a department
     */
    @Query("SELECT t FROM Teacher t WHERE t.department.name = :department")
    List<Teacher> findByDepartment(@Param("department") String department);

    /**
     * Find teachers with minimum years of experience
     */
    List<Teacher> findByYearsOfExperienceGreaterThanEqual(Integer years);
    
    /**
     * Find teachers with salary in range
     */
    List<Teacher> findBySalaryBetween(Double min, Double max);
    
    /**
     * Find teacher by ID with courses eagerly loaded
     */
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.courses WHERE t.id = :id")
    Optional<Teacher> findByIdWithCourses(@Param("id") Long id);
    
    /**
     * Find teachers with minimum number of courses
     */
    @Query("SELECT t FROM Teacher t WHERE SIZE(t.courses) >= :minCourses")
    List<Teacher> findTeachersWithMinCourses(@Param("minCourses") int minCourses);
    
    /**
     * Get average salary by department
     */
    @Query("SELECT AVG(t.salary) FROM Teacher t WHERE t.department.name = :dept")
    Double getAverageSalaryByDepartment(@Param("dept") String dept);
}
