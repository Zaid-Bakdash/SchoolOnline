package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced JUnit 5 tests for StudentRegistry
 * Demonstrates:
 * - Nested test classes for organization
 * - Parameterized tests with multiple sources
 * - Repeated tests
 * - Test lifecycle management
 * - Complex assertions
 */
@DisplayName("StudentRegistry Advanced Tests")
public class StudentRegistryAdvancedTest {
    
    private StudentRegistry registry;
    
    @BeforeEach
    public void setUp() {
        registry = new StudentRegistry();
    }
    
    @Nested
    @DisplayName("Parameterized Tests")
    class ParameterizedTestsDemo {
        
        @ParameterizedTest
        @ValueSource(strings = {"alice@school.edu", "bob@school.edu", "charlie@school.edu"})
        @DisplayName("Should add students with various emails")
        public void testAddStudentsWithVariousEmails(String email) {
            Student student = new Student("STU" + System.nanoTime(), "Test Student", email);
            registry.addStudent(student);
            assertEquals(1, registry.getStudentCount());
        }
        
        @ParameterizedTest
        @CsvSource({
            "STU001, Alice, alice@school.edu, 3.8",
            "STU002, Bob, bob@school.edu, 3.5",
            "STU003, Charlie, charlie@school.edu, 3.9",
            "STU004, Diana, diana@school.edu, 4.0"
        })
        @DisplayName("Should add students with various GPAs")
        public void testAddStudentsWithVariousGpas(String id, String name, String email, double gpa) {
            Student student = new Student(id, name, email, gpa);
            registry.addStudent(student);
            
            Student found = registry.findStudentById(id);
            assertNotNull(found);
            assertEquals(gpa, found.getGpa());
        }
    }
    
    @Nested
    @DisplayName("Repeated Tests")
    class RepeatedTestsDemo {
        
        @RepeatedTest(5)
        @DisplayName("Should handle repeated enrollment")
        public void testRepeatedEnrollment(RepetitionInfo repetitionInfo) {
            // Each repetition tests enrollment independently
            StudentRegistry tempRegistry = new StudentRegistry();
            Student student = new Student("STU001", "Test Student", "test@school.edu");
            tempRegistry.addStudent(student);
            assertEquals(1, tempRegistry.getStudentCount());
        }

        @RepeatedTest(10)
        @DisplayName("Concurrent enrollment stress test")
        public void testConcurrentEnrollment(RepetitionInfo repetitionInfo) throws InterruptedException {
            StudentRegistry sharedRegistry = new StudentRegistry();
            final int threadCount = 5;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                final int idx = i;
                executor.submit(() -> {
                    String id = String.format("R%d-T%d", repetitionInfo.getCurrentRepetition(), idx);
                    Student s = new Student(id, "Concurrent", "c" + idx + "@school.edu", 3.0 + (idx % 2));
                    // synchronize to keep test deterministic (registry is not thread-safe)
                    synchronized (sharedRegistry) {
                        sharedRegistry.addStudent(s);
                    }
                    latch.countDown();
                });
            }

            boolean finished = latch.await(5, TimeUnit.SECONDS);
            executor.shutdownNow();
            assertTrue(finished, "Concurrent tasks did not finish in time");
            assertEquals(threadCount, sharedRegistry.getStudentCount());
        }
    }
    
    @Nested
    @DisplayName("Search and Filter Tests")
    class SearchAndFilterTests {
        
        @BeforeEach
        public void setupStudents() {
            registry.addStudent(new Student("STU001", "Alice Johnson", "alice@school.edu", 3.8));
            registry.addStudent(new Student("STU002", "Bob Smith", "bob@school.edu", 3.5));
            registry.addStudent(new Student("STU003", "Charlie Brown", "charlie@school.edu", 3.9));
            registry.addStudent(new Student("STU004", "Diana Prince", "diana@school.edu", 4.0));
        }
        
        @Test
        @DisplayName("Should find all students with high GPA")
        public void testFindHighGpaStudents() {
            List<Student> highGpa = registry.getStudentsWithHighGpa(3.8);
            assertEquals(3, highGpa.size());
            
            // Verify all returned students have GPA >= 3.8
            highGpa.forEach(s -> assertTrue(s.getGpa() >= 3.8));
        }
        
        @Test
        @DisplayName("Should find students by partial name match")
        public void testFindByPartialName() {
            List<Student> results = registry.findStudentsByName("Charlie");
            assertEquals(1, results.size());
            assertEquals("Charlie Brown", results.get(0).getName());
        }
        
        @Test
        @DisplayName("Should find multiple students with partial name")
        public void testFindMultipleByPartialName() {
            List<Student> results = registry.findStudentsByName("a");
            assertEquals(3, results.size()); // Alice, Diana, Charlie
        }
    }
    
    @Nested
    @DisplayName("Sorting Tests")
    class SortingTests {
        
        @BeforeEach
        public void setupStudents() {
            registry.addStudent(new Student("STU003", "Charlie", "charlie@school.edu", 3.5));
            registry.addStudent(new Student("STU001", "Alice", "alice@school.edu", 3.8));
            registry.addStudent(new Student("STU002", "Bob", "bob@school.edu", 3.9));
        }
        
        @Test
        @DisplayName("Should sort students by name alphabetically")
        public void testSortByName() {
            List<Student> sorted = registry.getAllStudentsSortedByName();
            
            assertEquals("Alice", sorted.get(0).getName());
            assertEquals("Bob", sorted.get(1).getName());
            assertEquals("Charlie", sorted.get(2).getName());
        }
        
        @Test
        @DisplayName("Should sort students by GPA descending")
        public void testSortByGpaDescending() {
            List<Student> sorted = registry.getAllStudentsSortedByGpa();
            
            assertEquals(3.9, sorted.get(0).getGpa());
            assertEquals(3.8, sorted.get(1).getGpa());
            assertEquals(3.5, sorted.get(2).getGpa());
        }
    }
    
    @Nested
    @DisplayName("Statistics Tests")
    class StatisticsTests {
        
        @BeforeEach
        public void setupStudents() {
            registry.addStudent(new Student("STU001", "Alice", "alice@school.edu", 3.6));
            registry.addStudent(new Student("STU002", "Bob", "bob@school.edu", 3.4));
            registry.addStudent(new Student("STU003", "Charlie", "charlie@school.edu", 3.8));
        }
        
        @Test
        @DisplayName("Should calculate correct average GPA")
        public void testAverageGpa() {
            double average = registry.getAverageGpa();
            double expected = (3.6 + 3.4 + 3.8) / 3;
            assertEquals(expected, average, 0.01);
        }
        
        @Test
        @DisplayName("Should return 0 average for empty registry")
        public void testAverageGpaEmpty() {
            StudentRegistry emptyRegistry = new StudentRegistry();
            assertEquals(0.0, emptyRegistry.getAverageGpa());
        }
    }
    
    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("Should throw exception for null student")
        public void testAddNullStudent() {
            assertThrows(IllegalArgumentException.class, () -> registry.addStudent(null));
        }
        
        @Test
        @DisplayName("Should throw exception for invalid email")
        public void testAddStudentWithInvalidEmail() {
            Student invalidStudent = new Student("STU001", "Invalid", "invalid-email");
            assertThrows(IllegalArgumentException.class, () -> registry.addStudent(invalidStudent));
        }
        
        @Test
        @DisplayName("Should return null for non-existent student")
        public void testFindNonExistentStudent() {
            Student found = registry.findStudentById("NONEXISTENT");
            assertNull(found);
        }
    }
    
    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {
        
        @Test
        @DisplayName("Should maintain state after operations")
        public void testStateConsistency() {
            Student s1 = new Student("STU001", "Alice", "alice@school.edu");
            Student s2 = new Student("STU002", "Bob", "bob@school.edu");
            
            registry.addStudent(s1);
            registry.addStudent(s2);
            assertEquals(2, registry.getStudentCount());
            
            registry.removeStudent("STU001");
            assertEquals(1, registry.getStudentCount());
            
            assertNull(registry.findStudentById("STU001"));
            assertNotNull(registry.findStudentById("STU002"));
        }
        
        @Test
        @DisplayName("Should clear all students")
        public void testClearRegistry() {
            registry.addStudent(new Student("STU001", "Alice", "alice@school.edu"));
            registry.addStudent(new Student("STU002", "Bob", "bob@school.edu"));
            
            assertEquals(2, registry.getStudentCount());
            registry.clear();
            assertEquals(0, registry.getStudentCount());
        }
    }
}
