package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.*;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.EnrollmentRepository;
import com.bootcamp.onlineschool.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * EnrollmentService - Service layer for Enrollment entity
 */
@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Enroll a student in a course
     */
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + courseId));

        // Check if student is already enrolled in this course
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByStudent_IdAndCourse_Id(studentId, courseId);
        if (existingEnrollment.isPresent()) {
            throw new EnrollmentAlreadyExistsException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment(student, course, LocalDate.now(), EnrollmentStatus.ENROLLED);
        Enrollment saved = enrollmentRepository.save(enrollment);

        // Update bidirectional relationships
        student.addEnrollment(saved);
        course.addEnrollment(saved);

        return saved;
    }

    /**
     * Drop a course enrollment
     */
    public Enrollment dropCourse(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found: " + enrollmentId));

        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {
            throw new InvalidEnrollmentStatusException("Can only drop active enrollments");
        }

        enrollment.drop();
        return enrollmentRepository.save(enrollment);
    }

    /**
     * Complete an enrollment with a grade
     */
    public Enrollment completeEnrollment(Long enrollmentId, String grade) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found: " + enrollmentId));

        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {
            throw new InvalidEnrollmentStatusException("Can only complete active enrollments");
        }

        if (!isValidGrade(grade)) {
            throw new InvalidGradeException("Invalid grade: " + grade);
        }

        enrollment.complete(grade);
        return enrollmentRepository.save(enrollment);
    }

    /**
     * Withdraw from a course
     */
    public Enrollment withdrawFromCourse(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found: " + enrollmentId));

        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {
            throw new InvalidEnrollmentStatusException("Can only withdraw from active enrollments");
        }

        enrollment.withdraw();
        return enrollmentRepository.save(enrollment);
    }

    /**
     * Get all enrollments for a student
     */
    @Transactional(readOnly = true)
    public List<Enrollment> getStudentEnrollments(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Student not found: " + studentId);
        }
        return enrollmentRepository.findByStudent_Id(studentId);
    }

    /**
     * Get all enrollments for a course
     */
    @Transactional(readOnly = true)
    public List<Enrollment> getCourseEnrollments(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found: " + courseId);
        }
        return enrollmentRepository.findByCourse_Id(courseId);
    }

    /**
     * Get enrollments by status
     */
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollmentRepository.findByStatus(status);
    }

    /**
     * Get enrollment by student and course
     */
    @Transactional(readOnly = true)
    public Optional<Enrollment> getEnrollmentByStudentAndCourse(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudent_IdAndCourse_Id(studentId, courseId);
    }

    /**
     * Get enrollments within date range
     */
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return enrollmentRepository.findByEnrollmentDateBetween(startDate, endDate);
    }

    /**
     * Get enrollments by grade
     */
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByGrade(String grade) {
        return enrollmentRepository.findByGrade(grade);
    }

    /**
     * Count enrollments by status
     */
    @Transactional(readOnly = true)
    public long countEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollmentRepository.countByStatus(status);
    }

    /**
     * Get average grade for a course
     */
    @Transactional(readOnly = true)
    public Double getAverageGradeByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found: " + courseId);
        }
        return enrollmentRepository.getAverageGradeByCourse(courseId, EnrollmentStatus.COMPLETED);
    }

    /**
     * Delete enrollment
     */
    public void deleteEnrollment(Long enrollmentId) {
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new EnrollmentNotFoundException("Enrollment not found: " + enrollmentId);
        }
        enrollmentRepository.deleteById(enrollmentId);
    }

    /**
     * Validate grade format
     */
    private boolean isValidGrade(String grade) {
        return grade != null && grade.matches("[A-F]");
    }

    // Custom Exceptions
    public static class EnrollmentNotFoundException extends RuntimeException {
        public EnrollmentNotFoundException(String message) {
            super(message);
        }
    }

    public static class StudentNotFoundException extends RuntimeException {
        public StudentNotFoundException(String message) {
            super(message);
        }
    }

    public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(String message) {
            super(message);
        }
    }

    public static class EnrollmentAlreadyExistsException extends RuntimeException {
        public EnrollmentAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidEnrollmentStatusException extends RuntimeException {
        public InvalidEnrollmentStatusException(String message) {
            super(message);
        }
    }

    public static class InvalidGradeException extends RuntimeException {
        public InvalidGradeException(String message) {
            super(message);
        }
    }
}