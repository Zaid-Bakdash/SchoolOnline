package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EnrollmentRepository tests
 */
@DataJpaTest
@DisplayName("Enrollment Repository Tests")
public class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Student student1;
    private Student student2;
    private Course course1;
    private Course course2;
    private Enrollment enrollment1;
    private Enrollment enrollment2;
    private Enrollment enrollment3;

    @BeforeEach
    public void setUp() {
        student1 = studentRepository.save(new Student("STU001", "John Doe", "john@example.com", LocalDate.of(2023, 9, 1)));
        student2 = studentRepository.save(new Student("STU002", "Jane Smith", "jane@example.com", LocalDate.of(2023, 9, 1)));

        course1 = courseRepository.save(new Course("Java Programming", "Learn Java", 3, 30));
        course2 = courseRepository.save(new Course("Python Basics", "Learn Python", 3, 25));

        enrollment1 = new Enrollment(student1, course1, LocalDate.of(2024, 1, 15), EnrollmentStatus.ENROLLED);
        enrollment2 = new Enrollment(student1, course2, LocalDate.of(2024, 1, 16), EnrollmentStatus.COMPLETED);
        enrollment2.setGrade("A");
        enrollment3 = new Enrollment(student2, course1, LocalDate.of(2024, 1, 17), EnrollmentStatus.DROPPED);

        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);
        enrollmentRepository.save(enrollment3);
    }

    @Test
    @DisplayName("Should find enrollments by student ID")
    public void testFindByStudent_Id() {
        List<Enrollment> enrollments = enrollmentRepository.findByStudent_Id(student1.getId());
        assertEquals(2, enrollments.size());
        assertTrue(enrollments.stream().allMatch(e -> e.getStudent().getId().equals(student1.getId())));
    }

    @Test
    @DisplayName("Should find enrollments by course ID")
    public void testFindByCourse_Id() {
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_Id(course1.getId());
        assertEquals(2, enrollments.size());
        assertTrue(enrollments.stream().allMatch(e -> e.getCourse().getId().equals(course1.getId())));
    }

    @Test
    @DisplayName("Should find enrollments by status")
    public void testFindByStatus() {
        List<Enrollment> enrolled = enrollmentRepository.findByStatus(EnrollmentStatus.ENROLLED);
        assertEquals(1, enrolled.size());

        List<Enrollment> completed = enrollmentRepository.findByStatus(EnrollmentStatus.COMPLETED);
        assertEquals(1, completed.size());
    }

    @Test
    @DisplayName("Should find enrollment by student and course")
    public void testFindByStudent_IdAndCourse_Id() {
        Optional<Enrollment> enrollment = enrollmentRepository.findByStudent_IdAndCourse_Id(student1.getId(), course1.getId());
        assertTrue(enrollment.isPresent());
        assertEquals(student1.getId(), enrollment.get().getStudent().getId());
        assertEquals(course1.getId(), enrollment.get().getCourse().getId());
    }

    @Test
    @DisplayName("Should find enrollments by date range")
    public void testFindByEnrollmentDateBetween() {
        List<Enrollment> enrollments = enrollmentRepository.findByEnrollmentDateBetween(
                LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 16));
        assertEquals(2, enrollments.size());
    }

    @Test
    @DisplayName("Should find enrollments by grade")
    public void testFindByGrade() {
        List<Enrollment> enrollments = enrollmentRepository.findByGrade("A");
        assertEquals(1, enrollments.size());
        assertEquals("A", enrollments.get(0).getGrade());
    }

    @Test
    @DisplayName("Should count enrollments by status")
    public void testCountByStatus() {
        long enrolledCount = enrollmentRepository.countByStatus(EnrollmentStatus.ENROLLED);
        assertEquals(1, enrolledCount);

        long completedCount = enrollmentRepository.countByStatus(EnrollmentStatus.COMPLETED);
        assertEquals(1, completedCount);
    }

    @Test
    @DisplayName("Should find enrollments by course and status")
    public void testFindByCourse_IdAndStatus() {
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_IdAndStatus(course1.getId(), EnrollmentStatus.ENROLLED);
        assertEquals(1, enrollments.size());
        assertEquals(EnrollmentStatus.ENROLLED, enrollments.get(0).getStatus());
    }

    @Test
    @DisplayName("Should find enrollments by course with student")
    public void testFindByCourse_IdWithStudent() {
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_IdWithStudent(course1.getId());
        assertEquals(2, enrollments.size());
        // Verify that student is loaded (not null)
        assertTrue(enrollments.stream().allMatch(e -> e.getStudent() != null));
    }

    @Test
    @DisplayName("Should find enrollments by student with course")
    public void testFindByStudent_IdWithCourse() {
        List<Enrollment> enrollments = enrollmentRepository.findByStudent_IdWithCourse(student1.getId());
        assertEquals(2, enrollments.size());
        // Verify that course is loaded (not null)
        assertTrue(enrollments.stream().allMatch(e -> e.getCourse() != null));
    }

    @Test
    @DisplayName("Should count enrollments by student and status")
    public void testCountByStudent_IdAndStatus() {
        long count = enrollmentRepository.countByStudent_IdAndStatus(student1.getId(), EnrollmentStatus.COMPLETED);
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Should calculate average grade by course")
    public void testGetAverageGradeByCourse() {
        Double average = enrollmentRepository.getAverageGradeByCourse(course2.getId());
        assertNotNull(average);
        // Since only one enrollment is completed with grade A (4.0) for course2, average should be 4.0
        assertEquals(4.0, average);
    }

    @Test
    @DisplayName("Should save and retrieve enrollment")
    public void testSaveAndRetrieve() {
        Enrollment newEnrollment = new Enrollment(student2, course2, LocalDate.now(), EnrollmentStatus.ENROLLED);
        Enrollment saved = enrollmentRepository.save(newEnrollment);

        assertNotNull(saved.getId());
        Optional<Enrollment> retrieved = enrollmentRepository.findById(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(student2.getId(), retrieved.get().getStudent().getId());
    }
}