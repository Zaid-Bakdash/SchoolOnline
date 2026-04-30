package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Course;
import com.bootcamp.onlineschool.entity.Registration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RepositoryIntegrationTest demonstrates cross-repository queries and integration testing
 * 
 * Demonstrates:
 * - Multiple repository injection
 * - Cross-repository data consistency
 * - Complex filtering and sorting
 * - Aggregation queries
 * - Relationship operations
 * - Transaction behavior
 */
@DataJpaTest
@DisplayName("Repository Integration Tests")
public class RepositoryIntegrationTest {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    @BeforeEach
    public void setUp() {
        // Clear all repositories to ensure clean test state
        registrationRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Should verify cross-repository data consistency when creating registrations")
    public void testCrossRepositoryDataConsistency() {
        // Arrange
        Student student = new Student("STU001", "Alice Johnson", "alice@school.edu", 3.8);
        Course course = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        
        Student savedStudent = studentRepository.save(student);
        Course savedCourse = courseRepository.save(course);
        
        // Act
        Registration registration = new Registration(LocalDate.now(), "ACTIVE", savedStudent, savedCourse);
        Registration savedRegistration = registrationRepository.save(registration);
        
        // Assert
        assertNotNull(savedRegistration.getId());
        assertEquals(savedStudent.getId(), savedRegistration.getStudent().getId());
        assertEquals(savedCourse.getId(), savedRegistration.getCourse().getId());
        
        // Verify data can be retrieved from all repositories
        assertTrue(studentRepository.findById(savedStudent.getId()).isPresent());
        assertTrue(courseRepository.findById(savedCourse.getId()).isPresent());
        assertTrue(registrationRepository.findById(savedRegistration.getId()).isPresent());
    }
    
    @Test
    @DisplayName("Should retrieve registrations for specific student with aggregation")
    public void testRetrieveStudentRegistrationsWithAggregation() {
        // Arrange
        Student student = new Student("STU001", "Alice Johnson", "alice@school.edu", 3.8);
        Student savedStudent = studentRepository.save(student);
        
        Course course1 = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        Course course2 = new Course("CS102", "Advanced Java", 4, "Dr. Brown", 25);
        Course savedCourse1 = courseRepository.save(course1);
        Course savedCourse2 = courseRepository.save(course2);
        
        registrationRepository.save(new Registration(LocalDate.now(), "ACTIVE", savedStudent, savedCourse1));
        registrationRepository.save(new Registration(LocalDate.now(), "ACTIVE", savedStudent, savedCourse2));
        
        // Act
        List<Registration> registrations = registrationRepository.findByStudent(savedStudent);
        
        // Assert
        assertEquals(2, registrations.size());
        assertTrue(registrations.stream().allMatch(r -> r.getStudent().getId().equals(savedStudent.getId())));
    }
    
    @Test
    @DisplayName("Should verify complex filtering across repositories")
    public void testComplexFilteringAcrossRepositories() {
        // Arrange
        Student student1 = new Student("STU001", "Alice Johnson", "alice@school.edu", 3.8);
        Student student2 = new Student("STU002", "Bob Smith", "bob@school.edu", 3.2);
        Student student3 = new Student("STU003", "Charlie Brown", "charlie@school.edu", 3.5);
        
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        
        Course course = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        Course savedCourse = courseRepository.save(course);
        
        // Act - Register only high GPA students
        List<Student> highAchievers = studentRepository.findHighAchievers(3.5);
        
        // Assert
        assertEquals(2, highAchievers.size());
        assertTrue(highAchievers.stream().allMatch(s -> s.getGpa() >= 3.5));
    }
    
    @Test
    @DisplayName("Should handle multiple registrations and verify course enrollment counts")
    public void testMultipleRegistrationsAndEnrollmentCounts() {
        // Arrange
        Student student1 = new Student("STU001", "Alice", "alice@school.edu", 3.8);
        Student student2 = new Student("STU002", "Bob", "bob@school.edu", 3.5);
        Student student3 = new Student("STU003", "Charlie", "charlie@school.edu", 3.9);
        
        Course course = new Course("CS101", "Java Basics", 3, "Dr. Smith", 3);
        
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        Course savedCourse = courseRepository.save(course);
        
        // Act - Enroll students
        for (Student student : List.of(student1, student2, student3)) {
            registrationRepository.save(new Registration(LocalDate.now(), "ACTIVE", student, savedCourse));
            savedCourse.enrollStudent();
        }
        courseRepository.save(savedCourse);
        
        // Assert
        assertEquals(3, savedCourse.getEnrolledStudents());
        assertTrue(savedCourse.isFull());
        
        List<Course> fullCourses = courseRepository.findFullCourses();
        assertEquals(1, fullCourses.size());
    }
    
    @Test
    @DisplayName("Should test aggregation queries with multiple courses")
    public void testAggregationWithMultipleCourses() {
        // Arrange
        Course course1 = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        Course course2 = new Course("CS102", "Advanced Java", 4, "Dr. Brown", 25);
        Course course3 = new Course("CS103", "Web Dev", 3, "Dr. Johnson", 20);
        
        courseRepository.save(course1);
        courseRepository.save(course2);
        Course savedCourse3 = courseRepository.save(course3);
        
        // Enroll students
        for (int i = 0; i < 5; i++) {
            course1.enrollStudent();
            course2.enrollStudent();
            savedCourse3.enrollStudent();
        }
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(savedCourse3);
        
        // Act
        Long totalEnrollment = courseRepository.getTotalEnrollment();
        
        // Assert
        assertEquals(15L, totalEnrollment);
    }
    
    @Test
    @DisplayName("Should verify GPA range filtering with multiple students")
    public void testGpaRangeFilteringWithMultipleStudents() {
        // Arrange
        studentRepository.save(new Student("STU001", "Alice", "alice@school.edu", 3.2));
        studentRepository.save(new Student("STU002", "Bob", "bob@school.edu", 3.5));
        studentRepository.save(new Student("STU003", "Charlie", "charlie@school.edu", 3.8));
        studentRepository.save(new Student("STU004", "Diana", "diana@school.edu", 3.9));
        studentRepository.save(new Student("STU005", "Eve", "eve@school.edu", 3.1));
        
        // Act
        List<Student> studentsInRange = studentRepository.findStudentsByGpaRange(3.5, 3.9);
        
        // Assert
        assertEquals(3, studentsInRange.size());
        assertTrue(studentsInRange.stream().allMatch(s -> s.getGpa() >= 3.5 && s.getGpa() <= 3.9));
        assertEquals("Diana", studentsInRange.get(0).getName()); // Highest GPA first
    }
    
    @Test
    @DisplayName("Should retrieve top N students using pagination")
    public void testRetrieveTopNStudentsByGpa() {
        // Arrange
        studentRepository.save(new Student("STU001", "Alice", "alice@school.edu", 3.2));
        studentRepository.save(new Student("STU002", "Bob", "bob@school.edu", 3.5));
        studentRepository.save(new Student("STU003", "Charlie", "charlie@school.edu", 3.8));
        studentRepository.save(new Student("STU004", "Diana", "diana@school.edu", 3.9));
        
        // Act
        Pageable pageable = PageRequest.of(0, 2); // Get top 2 students
        List<Student> topStudents = studentRepository.findTopStudentsByGpa(pageable);
        
        // Assert
        assertTrue(topStudents.size() >= 2);
        assertEquals(3.9, topStudents.get(0).getGpa());
        assertEquals(3.8, topStudents.get(1).getGpa());
    }
    
    @Test
    @DisplayName("Should find courses with high enrollment rate")
    public void testFindCoursesWithHighEnrollmentRate() {
        // Arrange
        Course course1 = new Course("CS101", "Java Basics", 3, "Dr. Smith", 10);
        Course course2 = new Course("CS102", "Advanced Java", 4, "Dr. Brown", 30);
        
        // Enroll 8 students in course1 (80% enrollment)
        for (int i = 0; i < 8; i++) {
            course1.enrollStudent();
        }
        
        // Enroll 10 students in course2 (33% enrollment)
        for (int i = 0; i < 10; i++) {
            course2.enrollStudent();
        }
        
        courseRepository.save(course1);
        courseRepository.save(course2);
        
        // Act
        List<Course> highEnrollmentCourses = courseRepository.findCoursesWithEnrollmentRateAbove(0.75);
        
        // Assert
        assertEquals(1, highEnrollmentCourses.size());
        assertEquals("CS101", highEnrollmentCourses.get(0).getCourseId());
    }
    
    @Test
    @DisplayName("Should retrieve most popular courses using pagination")
    public void testRetrieveMostPopularCourses() {
        // Arrange
        Course course1 = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        Course course2 = new Course("CS102", "Advanced Java", 4, "Dr. Brown", 25);
        Course course3 = new Course("CS103", "Web Dev", 3, "Dr. Johnson", 20);
        
        // Set different enrollment levels
        for (int i = 0; i < 15; i++) {
            course1.enrollStudent();
        }
        for (int i = 0; i < 10; i++) {
            course2.enrollStudent();
        }
        for (int i = 0; i < 5; i++) {
            course3.enrollStudent();
        }
        
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        
        // Act
        Pageable pageable = PageRequest.of(0, 2); // Get top 2 popular courses
        List<Course> popularCourses = courseRepository.findMostPopularCourses(pageable);
        
        // Assert
        assertTrue(popularCourses.size() >= 2);
        assertEquals(15, popularCourses.get(0).getEnrolledStudents());
        assertEquals(10, popularCourses.get(1).getEnrolledStudents());
    }
    
    @Test
    @DisplayName("Should test transaction behavior with multiple registrations")
    public void testTransactionBehaviorWithMultipleOperations() {
        // Arrange
        Student student = new Student("STU001", "Alice", "alice@school.edu", 3.8);
        Course course1 = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        Course course2 = new Course("CS102", "Advanced Java", 4, "Dr. Brown", 25);
        
        Student savedStudent = studentRepository.save(student);
        Course savedCourse1 = courseRepository.save(course1);
        Course savedCourse2 = courseRepository.save(course2);
        
        // Act - Create multiple registrations in one transaction context
        Registration reg1 = registrationRepository.save(new Registration(LocalDate.now(), "ACTIVE", savedStudent, savedCourse1));
        Registration reg2 = registrationRepository.save(new Registration(LocalDate.now(), "ACTIVE", savedStudent, savedCourse2));
        
        // Update courses as students are enrolled
        savedCourse1.enrollStudent();
        savedCourse2.enrollStudent();
        courseRepository.save(savedCourse1);
        courseRepository.save(savedCourse2);
        
        // Assert - Verify all changes are persisted
        List<Registration> registrations = registrationRepository.findByStudent(savedStudent);
        assertEquals(2, registrations.size());
        
        Course retrievedCourse1 = courseRepository.findById(savedCourse1.getId()).orElse(null);
        assertNotNull(retrievedCourse1);
        assertEquals(1, retrievedCourse1.getEnrolledStudents());
    }
    
    @Test
    @DisplayName("Should verify cascading behavior and relationship integrity")
    public void testRelationshipIntegrityAcrossRepositories() {
        // Arrange
        Student student = new Student("STU001", "Alice", "alice@school.edu", 3.8);
        Course course = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        
        Student savedStudent = studentRepository.save(student);
        Course savedCourse = courseRepository.save(course);
        
        Registration registration = new Registration(LocalDate.now(), "ACTIVE", savedStudent, savedCourse);
        Registration savedRegistration = registrationRepository.save(registration);
        
        // Act - Verify relationship can be accessed bidirectionally
        Registration retrievedRegistration = registrationRepository.findById(savedRegistration.getId()).orElse(null);
        
        // Assert
        assertNotNull(retrievedRegistration);
        assertNotNull(retrievedRegistration.getStudent());
        assertNotNull(retrievedRegistration.getCourse());
        assertEquals("Alice", retrievedRegistration.getStudent().getName());
        assertEquals("Java Basics", retrievedRegistration.getCourse().getCourseName());
        assertEquals("STU001", retrievedRegistration.getStudent().getStudentId());
    }
}
