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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enrollment Integration tests - Complex scenarios with multiple entities
 */
@SpringBootTest
@DisplayName("Enrollment Integration Tests")
public class EnrollmentIntegrationTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @BeforeEach
    public void setUp() {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    @DisplayName("Should handle complete enrollment lifecycle")
    public void testCompleteEnrollmentLifecycle() {
        // Create student and course
        Student student = studentService.createStudent(new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1)));
        Course course = courseService.createCourse(new Course("Java Programming", "Learn Java", 3, 30));

        // Enroll student
        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), course.getId());
        assertEquals(EnrollmentStatus.ENROLLED, enrollment.getStatus());

        // Complete enrollment
        Enrollment completed = enrollmentService.completeEnrollment(enrollment.getId(), "A");
        assertEquals(EnrollmentStatus.COMPLETED, completed.getStatus());
        assertEquals("A", completed.getGrade());
        assertNotNull(completed.getCompletionDate());

        // Verify relationships are maintained
        Student updatedStudent = studentService.findStudentById(student.getId());
        Course updatedCourse = courseService.getCourseById(course.getId());

        assertEquals(1, updatedStudent.getEnrollments().size());
        assertEquals(1, updatedCourse.getEnrollments().size());
    }

    @Test
    @DisplayName("Should handle multiple students enrolling in multiple courses")
    public void testMultipleEnrollments() {
        // Create multiple students and courses
        Student student1 = studentService.createStudent(new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1)));
        Student student2 = studentService.createStudent(new Student("STU002", "Jane Smith", "jane@example.com", LocalDate.of(2023, 9, 1)));

        Course course1 = courseService.createCourse(new Course("Java Programming", "Learn Java", 3, 30));
        Course course2 = courseService.createCourse(new Course("Python Basics", "Learn Python", 3, 25));
        Course course3 = courseService.createCourse(new Course("Web Development", "Learn HTML/CSS/JS", 4, 40));

        // Create enrollments
        enrollmentService.enrollStudent(student1.getId(), course1.getId());
        enrollmentService.enrollStudent(student1.getId(), course2.getId());
        enrollmentService.enrollStudent(student2.getId(), course2.getId());
        enrollmentService.enrollStudent(student2.getId(), course3.getId());

        // Verify enrollments
        List<Enrollment> student1Enrollments = enrollmentService.getStudentEnrollments(student1.getId());
        List<Enrollment> student2Enrollments = enrollmentService.getStudentEnrollments(student2.getId());
        List<Enrollment> course2Enrollments = enrollmentService.getCourseEnrollments(course2.getId());

        assertEquals(2, student1Enrollments.size());
        assertEquals(2, student2Enrollments.size());
        assertEquals(2, course2Enrollments.size());

        // Complete some enrollments
        Enrollment enrollment = student1Enrollments.get(0);
        enrollmentService.completeEnrollment(enrollment.getId(), "B");

        // Verify completion
        List<Enrollment> completedEnrollments = enrollmentService.getEnrollmentsByStatus(EnrollmentStatus.COMPLETED);
        assertEquals(1, completedEnrollments.size());
        assertEquals("B", completedEnrollments.get(0).getGrade());
    }

    @Test
    @DisplayName("Should prevent duplicate enrollments")
    public void testPreventDuplicateEnrollments() {
        Student student = studentService.createStudent(new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1)));
        Course course = courseService.createCourse(new Course("Java Programming", "Learn Java", 3, 30));

        // First enrollment should succeed
        enrollmentService.enrollStudent(student.getId(), course.getId());

        // Second enrollment should fail
        assertThrows(EnrollmentService.EnrollmentAlreadyExistsException.class,
                () -> enrollmentService.enrollStudent(student.getId(), course.getId()));

        // Verify only one enrollment exists
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());
        assertEquals(1, enrollments.size());
    }

    @Test
    @DisplayName("Should handle enrollment status transitions")
    public void testEnrollmentStatusTransitions() {
        Student student = studentService.createStudent(new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1)));
        Course course = courseService.createCourse(new Course("Java Programming", "Learn Java", 3, 30));

        // Start with enrollment
        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), course.getId());
        assertEquals(EnrollmentStatus.ENROLLED, enrollment.getStatus());

        // Drop the course
        Enrollment dropped = enrollmentService.dropCourse(enrollment.getId());
        assertEquals(EnrollmentStatus.DROPPED, dropped.getStatus());

        // Try to complete a dropped enrollment (should fail)
        assertThrows(EnrollmentService.InvalidEnrollmentStatusException.class,
                () -> enrollmentService.completeEnrollment(enrollment.getId(), "A"));

        // Try to drop again (should fail)
        assertThrows(EnrollmentService.InvalidEnrollmentStatusException.class,
                () -> enrollmentService.dropCourse(enrollment.getId()));
    }

    @Test
    @DisplayName("Should calculate course statistics correctly")
    public void testCourseStatistics() {
        Student student1 = studentService.createStudent(new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1)));
        Student student2 = studentService.createStudent(new Student("STU002", "Jane Smith", "jane@example.com", LocalDate.of(2023, 9, 1)));
        Student student3 = studentService.createStudent(new Student("STU003", "Bob Johnson", "bob@example.com", LocalDate.of(2023, 9, 1)));

        Course course = courseService.createCourse(new Course("Java Programming", "Learn Java", 3, 30));

        // Create enrollments with different statuses
        Enrollment e1 = enrollmentService.enrollStudent(student1.getId(), course.getId());
        Enrollment e2 = enrollmentService.enrollStudent(student2.getId(), course.getId());
        Enrollment e3 = enrollmentService.enrollStudent(student3.getId(), course.getId());

        // Complete some enrollments
        enrollmentService.completeEnrollment(e1.getId(), "A");
        enrollmentService.completeEnrollment(e2.getId(), "B");
        enrollmentService.dropCourse(e3.getId());

        // Verify counts
        assertEquals(0, enrollmentService.countEnrollmentsByStatus(EnrollmentStatus.ENROLLED));
        assertEquals(2, enrollmentService.countEnrollmentsByStatus(EnrollmentStatus.COMPLETED));
        assertEquals(1, enrollmentService.countEnrollmentsByStatus(EnrollmentStatus.DROPPED));

        // Verify average grade (A=4.0, B=3.0, average = 3.5)
        Double averageGrade = enrollmentService.getAverageGradeByCourse(course.getId());
        assertEquals(3.5, averageGrade);
    }

    @Test
    @DisplayName("Should handle bidirectional relationship consistency")
    public void testBidirectionalRelationshipConsistency() {
        Student student = studentService.createStudent(new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1)));
        Course course = courseService.createCourse(new Course("Java Programming", "Learn Java", 3, 30));

        // Enroll student
        enrollmentService.enrollStudent(student.getId(), course.getId());

        // Verify relationships from both sides
        Student retrievedStudent = studentService.findStudentById(student.getId());
        Course retrievedCourse = courseService.getCourseById(course.getId());

        assertEquals(1, retrievedStudent.getEnrollments().size());
        assertEquals(1, retrievedCourse.getEnrollments().size());

        Enrollment studentEnrollment = retrievedStudent.getEnrollments().iterator().next();
        Enrollment courseEnrollment = retrievedCourse.getEnrollments().iterator().next();


        assertEquals(studentEnrollment, courseEnrollment);
        assertEquals(student.getId(), studentEnrollment.getStudent().getId());
        assertEquals(course.getId(), studentEnrollment.getCourse().getId());
    }

    @Test
    @DisplayName("Should handle enrollment date range queries")
    public void testEnrollmentDateRangeQueries() {
        Student student = studentService.createStudent(new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1)));
        Course course1 = courseService.createCourse(new Course("Java Programming", "Learn Java", 3, 30));
        Course course2 = courseService.createCourse(new Course("Python Basics", "Learn Python", 3, 25));

        // Create enrollments on different dates
        LocalDate date1 = LocalDate.of(2024, 1, 15);
        LocalDate date2 = LocalDate.of(2024, 2, 1);
        LocalDate date3 = LocalDate.of(2024, 3, 1);

        // Manually create enrollments with specific dates
        Enrollment e1 = new Enrollment(student, course1, date1, EnrollmentStatus.ENROLLED);
        Enrollment e2 = new Enrollment(student, course2, date2, EnrollmentStatus.ENROLLED);
        enrollmentRepository.save(e1);
        enrollmentRepository.save(e2);

        // Query by date range
        List<Enrollment> rangeEnrollments = enrollmentService.getEnrollmentsByDateRange(date1, date2);
        assertEquals(2, rangeEnrollments.size());

        List<Enrollment> singleDayEnrollments = enrollmentService.getEnrollmentsByDateRange(date1, date1);
        assertEquals(1, singleDayEnrollments.size());
    }

    @Test
    @DisplayName("Should handle cascade operations correctly")
    public void testCascadeOperations() {
        Student student = studentService.createStudent(new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1)));
        Course course = courseService.createCourse(new Course("Java Programming", "Learn Java", 3, 30));

        // Create enrollment
        enrollmentService.enrollStudent(student.getId(), course.getId());

        // Verify enrollment exists
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());
        assertEquals(1, enrollments.size());

        // Delete student - should cascade to delete enrollments due to orphanRemoval
        studentService.deleteStudent(student.getId());

        // Verify student is deleted
        assertThrows(StudentService.StudentNotFoundException.class,
                () -> studentService.findStudentById(student.getId()));

        // Verify course still exists
        assertDoesNotThrow(() -> courseService.getCourseById(course.getId()));

        // Verify enrollments are gone (since student was deleted)
        List<Enrollment> courseEnrollments = enrollmentService.getCourseEnrollments(course.getId());
        assertEquals(0, courseEnrollments.size());
    }
}