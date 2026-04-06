package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StudentRegistry Tests")
public class StudentRegistryTest {

    private StudentRegistry registry;

    @BeforeEach
    public void setUp() {
        registry = new StudentRegistry();
    }

    @Nested
    @DisplayName("SearchTests")
    class SearchTests {

        @BeforeEach
        public void setupStudents() {
            registry.addStudent(new Student("STU001", "Alice Johnson", "alice@school.edu", 3.8));
            registry.addStudent(new Student("STU002", "Bob Smith", "bob@school.edu", 3.5));
            registry.addStudent(new Student("STU003", "Charlie Brown", "charlie@school.edu", 3.9));
        }

        @Test
        @DisplayName("Should find student by partial name")
        public void testFindByPartialName() {
            List<Student> results = registry.findStudentsByName("Alice");
            assertEquals(1, results.size());
            assertEquals("Alice Johnson", results.get(0).getName());
        }

        @Test
        @DisplayName("Should return empty list when no match")
        public void testFindNoMatch() {
            List<Student> results = registry.findStudentsByName("Zoe");
            assertTrue(results.isEmpty());
        }
    }

    @Nested
    @DisplayName("SortingTests")
    class SortingTests {

        @BeforeEach
        public void setupStudents() {
            registry.addStudent(new Student("STU003", "Charlie", "charlie@school.edu", 3.5));
            registry.addStudent(new Student("STU001", "Alice", "alice@school.edu", 3.8));
            registry.addStudent(new Student("STU002", "Bob", "bob@school.edu", 3.9));
        }

        @Test
        @DisplayName("Should sort by name")
        public void testSortByName() {
            List<Student> sorted = registry.getAllStudentsSortedByName();
            assertEquals("Alice", sorted.get(0).getName());
            assertEquals("Bob", sorted.get(1).getName());
            assertEquals("Charlie", sorted.get(2).getName());
        }

        @Test
        @DisplayName("Should sort by GPA descending")
        public void testSortByGpa() {
            List<Student> sorted = registry.getAllStudentsSortedByGpa();
            assertEquals(3.9, sorted.get(0).getGpa());
            assertEquals(3.8, sorted.get(1).getGpa());
            assertEquals(3.5, sorted.get(2).getGpa());
        }
    }

    @Nested
    @DisplayName("StatisticsTests")
    class StatisticsTests {

        @BeforeEach
        public void setupStudents() {
            registry.addStudent(new Student("STU001", "Alice", "alice@school.edu", 3.6));
            registry.addStudent(new Student("STU002", "Bob", "bob@school.edu", 3.4));
            registry.addStudent(new Student("STU003", "Charlie", "charlie@school.edu", 3.8));
        }

        @Test
        @DisplayName("Should calculate average GPA")
        public void testAverageGpa() {
            double avg = registry.getAverageGpa();
            double expected = (3.6 + 3.4 + 3.8) / 3;
            assertEquals(expected, avg, 0.01);
        }

        @Test
        @DisplayName("Should return 0 for empty registry")
        public void testAverageGpaEmpty() {
            StudentRegistry empty = new StudentRegistry();
            assertEquals(0.0, empty.getAverageGpa());
        }
    }

    @Nested
    @DisplayName("ErrorHandlingTests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw when adding null student")
        public void testAddNullStudent() {
            assertThrows(IllegalArgumentException.class, () -> registry.addStudent(null));
        }

        @Test
        @DisplayName("Should throw when adding invalid email")
        public void testInvalidEmail() {
            Student s = new Student("STU001", "Invalid", "invalid-email");
            assertThrows(IllegalArgumentException.class, () -> registry.addStudent(s));
        }

        @Test
        @DisplayName("Remove non-existent student returns false")
        public void testRemoveNonExistent() {
            assertFalse(registry.removeStudent("NOPE"));
        }

        @Test
        @DisplayName("Find with null ID returns null")
        public void testFindNullIdReturnsNull() {
            assertNull(registry.findStudentById(null));
        }

        @Test
        @DisplayName("Remove with null ID returns false")
        public void testRemoveNullIdReturnsFalse() {
            assertFalse(registry.removeStudent(null));
        }
    }
}
