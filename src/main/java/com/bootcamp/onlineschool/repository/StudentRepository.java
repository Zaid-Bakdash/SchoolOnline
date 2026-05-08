package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Student Repository Interface
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Find student by studentId
     */
    Optional<Student> findByStudentId(String studentId);

    /**
     * Find student along with enrollments
     */
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.enrollments WHERE s.id = :id")
    Optional<Student> findByIdWithEnrollments(@Param("id") Long id);

    /**
     * Check if student exists by studentId
     */
    boolean existsByStudentId(String studentId);

    /**
     * Find students by name containing (case insensitive)
     */
    List<Student> findByNameContainingIgnoreCase(String name);

    /**
     * Find students by email
     */
    Optional<Student> findByEmail(String email);

    /**
     * Check if student exists by email
     */
    boolean existsByEmail(String email);

    /**
     * Count total students
     */
    long count();
}