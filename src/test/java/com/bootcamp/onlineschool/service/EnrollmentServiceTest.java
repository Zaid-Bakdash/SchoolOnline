package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.*;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.EnrollmentRepository;
import com.bootcamp.onlineschool.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EnrollmentService tests
 */
@SpringBootTest
@DisplayName("Enrollment Service Tests")
public class EnrollmentServiceTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private Student student;
    private Course course;

    @BeforeEach
    public void setUp() {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();

        student = studentRepository.save(new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1)));
        course = courseRepository.save(new Course("Java Programming", "Learn Java", 3, 30));
    }

    @Test
    @DisplayName("Should enroll student in course successfully")
    public void testEnrollStudent() {
        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), course.getId());

        assertNotNull(enrollment.getId());
        assertEquals(student.getId(), enrollment.getStudent().getId());
        assertEquals(course.getId(), enrollment.getCourse().getId());
        assertEquals(EnrollmentStatus.ENROLLED, enrollment.getStatus());
        assertNotNull(enrollment.getEnrollmentDate());
    }

    @Test
    @DisplayName("Should throw exception when enrolling non-existent student")
    public void testEnrollStudentNotFound() {
        assertThrows(EnrollmentService.StudentNotFoundException.class,
                () -> enrollmentService.enrollStudent(999L, course.getId()));
    }

    @Test
    @DisplayName("Should throw exception when enrolling in non-existent course")
    public void testEnrollCourseNotFound() {
        assertThrows(EnrollmentService.CourseNotFoundException.class,
                () -> enrollmentService.enrollStudent(student.getId(), 999L));
    }

    @Test
    @DisplayName("Should throw exception when student already enrolled in course")
    public void testEnrollStudentAlreadyEnrolled() {
        enrollmentService.enrollStudent(student.getId(), course.getId());

        assertThrows(EnrollmentService.EnrollmentAlreadyExistsException.class,
                () -> enrollmentService.enrollStudent(student.getId(), course.getId()));
    }

    @Test
    @DisplayName("Should drop course enrollment")
    public void testDropCourse() {
        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), course.getId());
        Enrollment dropped = enrollmentService.dropCourse(enrollment.getId());

        assertEquals(EnrollmentStatus.DROPPED, dropped.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when dropping non-existent enrollment")
    public void testDropCourseNotFound() {
        assertThrows(EnrollmentService.EnrollmentNotFoundException.class,
                () -> enrollmentService.dropCourse(999L));
    }

    @Test
    @DisplayName("Should throw exception when dropping non-active enrollment")
    public void testDropCourseNotActive() {
        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), course.getId());
        enrollmentService.dropCourse(enrollment.getId());

        assertThrows(EnrollmentService.InvalidEnrollmentStatusException.class,
                () -> enrollmentService.dropCourse(enrollment.getId()));
    }

    @Test
    @DisplayName("Should complete enrollment with grade")
    public void testCompleteEnrollment() {
        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), course.getId());
        Enrollment completed = enrollmentService.completeEnrollment(enrollment.getId(), "A");

        assertEquals(EnrollmentStatus.COMPLETED, completed.getStatus());
        assertEquals("A", completed.getGrade());
        assertNotNull(completed.getCompletionDate());
    }

    @Test
    @DisplayName("Should throw exception for invalid grade")
    public void testCompleteEnrollmentInvalidGrade() {
        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), course.getId());

        assertThrows(EnrollmentService.InvalidGradeException.class,
                () -> enrollmentService.completeEnrollment(enrollment.getId(), "Z"));
    }

    @Test
    @DisplayName("Should withdraw from course")
    public void testWithdrawFromCourse() {
        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), course.getId());
        Enrollment withdrawn = enrollmentService.withdrawFromCourse(enrollment.getId());

        assertEquals(EnrollmentStatus.WITHDRAWN, withdrawn.getStatus());
    }

    @Test
    @DisplayName("Should get student enrollments")
    public void testGetStudentEnrollments() {
        enrollmentService.enrollStudent(student.getId(), course.getId());
        Course course2 = courseRepository.save(new Course("Python Basics", "Learn Python", 3, 25));
        enrollmentService.enrollStudent(student.getId(), course2.getId());

        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());
        assertEquals(2, enrollments.size());
    }

    @Test
    @DisplayName("Should get course enrollments")
    public void testGetCourseEnrollments() {
        enrollmentService.enrollStudent(student.getId(), course.getId());
        Student student2 = studentRepository.save(new Student("STU002", "Jane Smith", "jane@example.com", LocalDate.of(2023, 9, 1)));
        enrollmentService.enrollStudent(student2.getId(), course.getId());

        List<Enrollment> enrollments = enrollmentService.getCourseEnrollments(course.getId());
        assertEquals(2, enrollments.size());
    }

    @Test
    @DisplayName("Should get enrollments by status")
    public void testGetEnrollmentsByStatus() {
        enrollmentService.enrollStudent(student.getId(), course.getId());

        List<Enrollment> enrolled = enrollmentService.getEnrollmentsByStatus(EnrollmentStatus.ENROLLED);
        assertEquals(1, enrolled.size());
    }

    @Test
    @DisplayName("Should get enrollment by student and course")
    public void testGetEnrollmentByStudentAndCourse() {
        enrollmentService.enrollStudent(student.getId(), course.getId());

        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentByStudentAndCourse(student.getId(), course.getId());
        assertTrue(enrollment.isPresent());
        assertEquals(student.getId(), enrollment.get().getStudent().getId());
        assertEquals(course.getId(), enrollment.get().getCourse().getId());
    }

    @Test
    @DisplayName("Should count enrollments by status")
    public void testCountEnrollmentsByStatus() {
        enrollmentService.enrollStudent(student.getId(), course.getId());

        long count = enrollmentService.countEnrollmentsByStatus(EnrollmentStatus.ENROLLED);
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Should get average grade by course")
    public void testGetAverageGradeByCourse() {
        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), course.getId());
        enrollmentService.completeEnrollment(enrollment.getId(), "A");

        Double average = enrollmentService.getAverageGradeByCourse(course.getId());
        assertEquals(4.0, average);
    }
}