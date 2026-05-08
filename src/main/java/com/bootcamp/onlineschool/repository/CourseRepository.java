package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Course Repository Interface
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Find courses by name containing (case insensitive)
     */
    List<Course> findByNameContainingIgnoreCase(String name);

    /**
     * Find courses by credits
     */
    List<Course> findByCredits(int credits);

    /**
     * Find courses by credits range
     */
    List<Course> findByCreditsBetween(int minCredits, int maxCredits);

    /**
     * Find courses by duration
     */
    List<Course> findByDuration(int duration);

    /**
     * Find courses by credits or duration
     */
    @Query("SELECT c FROM Course c WHERE c.credits = :credits OR c.duration = :duration")
    List<Course> findByCreditsOrDuration(@Param("credits") int credits, @Param("duration") int duration);

    /**
     * Count total courses
     */
    long count();

    /**
     * Find course along with enrollments
     */
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.enrollments WHERE c.id = :id")
    Optional<Course> findByIdWithEnrollments(@Param("id") Long id);
}