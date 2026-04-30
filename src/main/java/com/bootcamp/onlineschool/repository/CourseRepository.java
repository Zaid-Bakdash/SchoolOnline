package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CourseRepository demonstrates Spring Data JPA repository pattern
 * 
 * Demonstrates:
 * - JpaRepository interface
 * - Custom query methods
 * - JPQL queries
 * - Named parameters
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    /**
     * Find course by course ID
     */
    Optional<Course> findByCourseId(String courseId);
    
    /**
     * Find courses by instructor name
     */
    List<Course> findByInstructor(String instructor);
    
    /**
     * Find courses by name (case-insensitive)
     */
    List<Course> findByCourseNameContainingIgnoreCase(String courseName);
    
    /**
     * Find available courses (not full)
     */
    @Query("SELECT c FROM Course c WHERE c.enrolledStudents < c.maxStudents ORDER BY c.courseName ASC")
    List<Course> findAvailableCourses();
    
    /**
     * Find full courses
     */
    @Query("SELECT c FROM Course c WHERE c.enrolledStudents >= c.maxStudents")
    List<Course> findFullCourses();
    
    /**
     * Find courses with available seats
     */
    @Query("SELECT c FROM Course c WHERE (c.maxStudents - c.enrolledStudents) >= :minSeats")
    List<Course> findCoursesWithMinimumSeats(@Param("minSeats") Integer minSeats);
    
    /**
     * Count available courses
     */
    @Query("SELECT COUNT(c) FROM Course c WHERE c.enrolledStudents < c.maxStudents")
    Long countAvailableCourses();
    
    /**
     * Get total enrollment across all courses
     */
    @Query("SELECT SUM(c.enrolledStudents) FROM Course c")
    Long getTotalEnrollment();
    
    /**
     * Find courses by instructor name
     */
    @Query("SELECT c FROM Course c WHERE c.instructor = :instructor")
    List<Course> findCoursesByInstructor(@Param("instructor") String instructor);
    
    /**
     * Find courses with enrollment rate above threshold
     */
    @Query("SELECT c FROM Course c WHERE (CAST(c.enrolledStudents AS DOUBLE) / c.maxStudents) >= :enrollmentRate")
    List<Course> findCoursesWithEnrollmentRateAbove(@Param("enrollmentRate") Double enrollmentRate);
    
    /**
     * Find most popular courses (highest enrollment)
     */
    @Query(value = "SELECT c FROM Course c ORDER BY c.enrolledStudents DESC")
    List<Course> findMostPopularCourses(org.springframework.data.domain.Pageable pageable);
}
