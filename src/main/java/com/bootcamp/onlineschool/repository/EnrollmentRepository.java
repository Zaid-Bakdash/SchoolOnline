package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.model.Enrollment;
import com.bootcamp.onlineschool.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * EnrollmentRepository - JPA Repository for Enrollment entities
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudent_Id(Long studentId);

    List<Enrollment> findByCourse_Id(Long courseId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    Optional<Enrollment> findByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.enrollmentDate BETWEEN :startDate AND :endDate")
    List<Enrollment> findByEnrollmentDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT e FROM Enrollment e WHERE e.grade = :grade")
    List<Enrollment> findByGrade(@Param("grade") String grade);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.status = :status")
    long countByStatus(@Param("status") EnrollmentStatus status);

    List<Enrollment> findByCourse_IdAndStatus(Long courseId, EnrollmentStatus status);

    @Query("SELECT e FROM Enrollment e LEFT JOIN FETCH e.student WHERE e.course.id = :courseId")
    List<Enrollment> findByCourse_IdWithStudent(@Param("courseId") Long courseId);

    @Query("SELECT e FROM Enrollment e LEFT JOIN FETCH e.course WHERE e.student.id = :studentId")
    List<Enrollment> findByStudent_IdWithCourse(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId AND e.status = :status")
    long countByStudent_IdAndStatus(@Param("studentId") Long studentId, @Param("status") EnrollmentStatus status);

    default Double getAverageGradeByCourse(Long courseId, EnrollmentStatus status) {
        var enrollments = findByCourse_IdAndStatus(courseId, status);
        return enrollments.stream()
                .filter(e -> e.getGrade() != null)
                .mapToDouble(e -> switch (e.getGrade()) {
                    case "A" -> 4.0;
                    case "B" -> 3.0;
                    case "C" -> 2.0;
                    case "D" -> 1.0;
                    default -> 0.0;
                })
                .average()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }

    default Double getAverageGradeByCourse(Long courseId) {
        return getAverageGradeByCourse(courseId, EnrollmentStatus.COMPLETED);
    }
}