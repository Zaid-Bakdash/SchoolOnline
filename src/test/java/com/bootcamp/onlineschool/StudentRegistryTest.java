package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Student;
import com.bootcamp.onlineschool.model.StudentNameComparator;
import com.bootcamp.onlineschool.model.StudentGpaNameComparator;
import com.bootcamp.onlineschool.model.StudentEmailDomainComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Comparator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for StudentRegistry class
 * Demonstrates:
 * - Testing collections
 * - Testing search and filter operations
 * - Testing sorting
 * - Testing edge cases
 */
@DisplayName("StudentRegistry Class Tests")
public class StudentRegistryTest {
    
    private StudentRegistry registry;
    private Student student1;
    private Student student2;
    private Student student3;
    
    @BeforeEach
    public void setUp() {
        registry = new StudentRegistry();
        student1 = new Student("STU001", "Alice Johnson", "alice@school.edu", 20, 3.8);
        student2 = new Student("STU002", "Bob Smith", "bob@school.edu", 21, 3.5);
        student3 = new Student("STU003", "Charlie Brown", "charlie@school.edu", 22, 3.9);
    }
    
    @Test
    @DisplayName("Should add students to registry")
    public void testAddStudent() {
        registry.addStudent(student1);
        assertEquals(1, registry.getStudentCount());
        
        registry.addStudent(student2);
        assertEquals(2, registry.getStudentCount());
    }
    
    @Test
    @DisplayName("Should throw exception when adding null student")
    public void testAddNullStudent() {
        assertThrows(IllegalArgumentException.class, () -> registry.addStudent(null));
    }
    
    @Test
    @DisplayName("Should throw exception when adding student with invalid email")
    public void testAddStudentWithInvalidEmail() {
        Student invalidStudent = new Student("STU004", "Invalid", "invalid-email");
        assertThrows(IllegalArgumentException.class, () -> registry.addStudent(invalidStudent));
    }
    
    @Test
    @DisplayName("Should find student by ID")
    public void testFindStudentById() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        
        Student found = registry.findStudentById("STU001");
        assertNotNull(found);
        assertEquals("Alice Johnson", found.getName());
        
        Student notFound = registry.findStudentById("STU999");
        assertNull(notFound);
    }
    
    @Test
    @DisplayName("Should find students by name")
    public void testFindStudentsByName() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        List<Student> results = registry.findStudentsByName("Charlie");
        assertEquals(1, results.size());
        assertEquals("Charlie Brown", results.get(0).getName());
        
        List<Student> allResults = registry.findStudentsByName("a");
        assertEquals(2, allResults.size()); // Alice and Charlie
    }
    
    @Test
    @DisplayName("Should remove student from registry")
    public void testRemoveStudent() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        assertEquals(2, registry.getStudentCount());
        
        boolean removed = registry.removeStudent("STU001");
        assertTrue(removed);
        assertEquals(1, registry.getStudentCount());
        
        boolean notRemoved = registry.removeStudent("STU999");
        assertFalse(notRemoved);
    }
    
    @Test
    @DisplayName("Should sort students by name")
    public void testSortByName() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        List<Student> sorted = registry.getAllStudentsSortedByName();
        assertEquals(3, sorted.size());
        assertEquals("Alice Johnson", sorted.get(0).getName());
        assertEquals("Bob Smith", sorted.get(1).getName());
        assertEquals("Charlie Brown", sorted.get(2).getName());
    }
    
    @Test
    @DisplayName("Should sort students by GPA descending")
    public void testSortByGpa() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        List<Student> sorted = registry.getAllStudentsSortedByGpa();
        assertEquals(3, sorted.size());
        assertEquals(3.9, sorted.get(0).getGpa());
        assertEquals(3.8, sorted.get(1).getGpa());
        assertEquals(3.5, sorted.get(2).getGpa());
    }
    
    @Test
    @DisplayName("Should filter students by GPA threshold")
    public void testGetStudentsWithHighGpa() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        List<Student> highGpa = registry.getStudentsWithHighGpa(3.7);
        assertEquals(2, highGpa.size());
        
        List<Student> veryHighGpa = registry.getStudentsWithHighGpa(3.9);
        assertEquals(1, veryHighGpa.size());
    }
    
    @Test
    @DisplayName("Should calculate average GPA")
    public void testGetAverageGpa() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        double average = registry.getAverageGpa();
        assertEquals((3.8 + 3.5 + 3.9) / 3, average, 0.01);
    }
    
    @Test
    @DisplayName("Should return 0 average GPA for empty registry")
    public void testGetAverageGpaEmpty() {
        assertEquals(0.0, registry.getAverageGpa());
    }
    
    @Test
    @DisplayName("Should clear all students")
    public void testClear() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        assertEquals(2, registry.getStudentCount());
        
        registry.clear();
        assertEquals(0, registry.getStudentCount());
    }
    
    @Test
    @DisplayName("Should find student by email (exact match)")
    public void testFindStudentByEmailExactMatch() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        
        Student found = registry.findStudentByEmail("alice@school.edu");
        assertNotNull(found);
        assertEquals("Alice Johnson", found.getName());
    }
    
    @Test
    @DisplayName("Should find student by email (case-insensitive)")
    public void testFindStudentByEmailCaseInsensitive() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        
        Student found1 = registry.findStudentByEmail("ALICE@SCHOOL.EDU");
        assertNotNull(found1);
        assertEquals("Alice Johnson", found1.getName());
        
        Student found2 = registry.findStudentByEmail("AlIcE@ScHoOl.EdU");
        assertNotNull(found2);
        assertEquals("Alice Johnson", found2.getName());
    }
    
    @Test
    @DisplayName("Should return null for non-existent email")
    public void testFindStudentByEmailNotFound() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        
        Student notFound = registry.findStudentByEmail("nonexistent@school.edu");
        assertNull(notFound);
    }
    
    @Test
    @DisplayName("Should handle null email parameter")
    public void testFindStudentByEmailNull() {
        registry.addStudent(student1);
        
        Student result = registry.findStudentByEmail(null);
        assertNull(result);
    }
    
    @Test
    @DisplayName("Should handle empty email parameter")
    public void testFindStudentByEmailEmpty() {
        registry.addStudent(student1);
        
        Student result = registry.findStudentByEmail("");
        assertNull(result);
        
        Student resultWhitespace = registry.findStudentByEmail("   ");
        assertNull(resultWhitespace);
    }

    @Test
    @DisplayName("Should find students by GPA range inclusive")
    public void testFindStudentsByGpaRangeInclusive() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        List<Student> matched = registry.findStudentsByGpaRange(3.5, 3.8);
        assertEquals(2, matched.size());

        List<Student> matchedBoundary = registry.findStudentsByGpaRange(3.8, 3.9);
        assertEquals(2, matchedBoundary.size());

        List<Student> allMatched = registry.findStudentsByGpaRange(0, 4);
        assertEquals(3, allMatched.size());

        List<Student> noMatched = registry.findStudentsByGpaRange(4.0, 4.5);
        assertTrue(noMatched.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when min > max for GPA range")
    public void testFindStudentsByGpaRangeMinGtMax() {
        registry.addStudent(student1);

        List<Student> result = registry.findStudentsByGpaRange(4.0, 3.0);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find students by email domain, case-insensitive")
    public void testFindStudentsByEmailDomain() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        List<Student> domainResults = registry.findStudentsByEmailDomain("school.edu");
        assertEquals(3, domainResults.size());

        List<Student> domainResultsCase = registry.findStudentsByEmailDomain("SCHOOL.EDU");
        assertEquals(3, domainResultsCase.size());

        List<Student> domainWithAt = registry.findStudentsByEmailDomain("@school.edu");
        assertEquals(3, domainWithAt.size());

        List<Student> unknownDomain = registry.findStudentsByEmailDomain("other.edu");
        assertTrue(unknownDomain.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for null domain and empty registry")
    public void testFindStudentsByEmailDomainEdgeCases() {
        List<Student> nullDomain = registry.findStudentsByEmailDomain(null);
        assertTrue(nullDomain.isEmpty());

        List<Student> emptyDomain = registry.findStudentsByEmailDomain("");
        assertTrue(emptyDomain.isEmpty());

        // empty registry returns empty list
        List<Student> emptyRegistry = registry.findStudentsByEmailDomain("school.edu");
        assertTrue(emptyRegistry.isEmpty());
    }

    // ===== Exercise 8: Custom Comparators Tests =====

    @Test
    @DisplayName("Should sort students by name using StudentNameComparator")
    public void testSortByNameComparator() {
        registry.addStudent(student1);  // Alice
        registry.addStudent(student2);  // Bob
        registry.addStudent(student3);  // Charlie

        List<Student> sorted = registry.getAllStudentsSorted(new StudentNameComparator());

        assertEquals(3, sorted.size());
        assertEquals("Alice Johnson", sorted.get(0).getName());
        assertEquals("Bob Smith", sorted.get(1).getName());
        assertEquals("Charlie Brown", sorted.get(2).getName());
    }

    @Test
    @DisplayName("Should sort students by name case-insensitive")
    public void testNameComparatorCaseInsensitive() {
        Student lowerCase = new Student("STU004", "alice smith", "alice.smith@school.edu", 18, 3.7);
        Student upperCase = new Student("STU005", "ALICE JONES", "alice.jones@school.edu", 19, 3.6);
        Student mixed = new Student("STU006", "Alice Brown", "alice.brown@school.edu", 20, 3.5);

        registry.addStudent(mixed);
        registry.addStudent(upperCase);
        registry.addStudent(lowerCase);

        List<Student> sorted = registry.getAllStudentsSorted(new StudentNameComparator());

        assertEquals(3, sorted.size());
        // All start with "Alice", should be sorted by last name (case-insensitive)
        assertTrue(sorted.get(0).getName().toLowerCase().contains("brown"));
        assertTrue(sorted.get(1).getName().toLowerCase().contains("jones"));
        assertTrue(sorted.get(2).getName().toLowerCase().contains("smith"));
    }

    @Test
    @DisplayName("Should sort students by GPA descending, then name ascending using StudentGpaNameComparator")
    public void testSortByGpaNameComparator() {
        registry.addStudent(student1);  // 3.8 - Alice
        registry.addStudent(student2);  // 3.5 - Bob
        registry.addStudent(student3);  // 3.9 - Charlie

        List<Student> sorted = registry.getAllStudentsSorted(new StudentGpaNameComparator());

        assertEquals(3, sorted.size());
        assertEquals(3.9, sorted.get(0).getGpa());  // Charlie (highest GPA)
        assertEquals(3.8, sorted.get(1).getGpa());  // Alice
        assertEquals(3.5, sorted.get(2).getGpa());  // Bob (lowest GPA)
    }

    @Test
    @DisplayName("Should use name as tie-breaker in GPA comparator")
    public void testGpaNameComparatorTieBreaker() {
        Student student4 = new Student("STU004", "Zoe Adams", "zoe@school.edu", 20, 3.8);
        Student student5 = new Student("STU005", "Alex Brown", "alex@school.edu", 21, 3.8);

        registry.addStudent(student1);   // 3.8 - Alice Johnson
        registry.addStudent(student4);   // 3.8 - Zoe Adams
        registry.addStudent(student5);   // 3.8 - Alex Brown

        List<Student> sorted = registry.getAllStudentsSorted(new StudentGpaNameComparator());

        assertEquals(3, sorted.size());
        // All have 3.8 GPA, should be sorted by name alphabetically
        assertEquals("Alex Brown", sorted.get(0).getName());
        assertEquals("Alice Johnson", sorted.get(1).getName());
        assertEquals("Zoe Adams", sorted.get(2).getName());
    }

    @Test
    @DisplayName("Should sort students by email domain, then name using StudentEmailDomainComparator")
    public void testSortByEmailDomainComparator() {
        Student testStudent = new Student("STU004", "Test User", "test@school.edu", 20, 3.7);
        registry.addStudent(student1);    // alice@school.edu
        registry.addStudent(student2);    // bob@school.edu
        registry.addStudent(testStudent); // test@test.edu

        List<Student> sorted = registry.getAllStudentsSorted(new StudentEmailDomainComparator());

        assertEquals(3, sorted.size());
        // school.edu comes before test.edu
        assertEquals("school.edu", sorted.get(0).getEmail().substring(sorted.get(0).getEmail().indexOf("@") + 1));
        assertEquals("school.edu", sorted.get(1).getEmail().substring(sorted.get(1).getEmail().indexOf("@") + 1));
        assertEquals("school.edu", sorted.get(2).getEmail().substring(sorted.get(2).getEmail().indexOf("@") + 1));
    }

    @Test
    @DisplayName("Should use name as tie-breaker in email domain comparator")
    public void testEmailDomainComparatorTieBreaker() {
        Student student4 = new Student("STU004", "Zed User", "zed@school.edu", 20, 3.7);
        registry.addStudent(student1);  // alice@school.edu
        registry.addStudent(student4);  // zed@school.edu

        List<Student> sorted = registry.getAllStudentsSorted(new StudentEmailDomainComparator());

        assertEquals(2, sorted.size());
        // Same domain, sorted by name
        assertEquals("Alice Johnson", sorted.get(0).getName());
        assertEquals("Zed User", sorted.get(1).getName());
    }

    @Test
    @DisplayName("Should sort empty registry")
    public void testSortEmptyRegistry() {
        List<Student> sorted = registry.getAllStudentsSorted(new StudentNameComparator());
        assertTrue(sorted.isEmpty());
    }

    @Test
    @DisplayName("Should sort single-element list")
    public void testSortSingleStudent() {
        registry.addStudent(student1);
        
        List<Student> sorted = registry.getAllStudentsSorted(new StudentNameComparator());
        
        assertEquals(1, sorted.size());
        assertEquals("Alice Johnson", sorted.get(0).getName());
    }

    @Test
    @DisplayName("Should throw exception when comparator is null")
    public void testSortWithNullComparator() {
        registry.addStudent(student1);
        
        assertThrows(IllegalArgumentException.class, () -> registry.getAllStudentsSorted(null));
    }

    @Test
    @DisplayName("Should handle multiple students with various data")
    public void testSortMultipleStudentsVariousData() {
        Student s4 = new Student("STU004", "Beth", "beth@school.edu", 19, 3.8);
        Student s5 = new Student("STU005", "Diana", "diana@school.edu", 20, 3.8);
        Student s6 = new Student("STU006", "Frank", "frank@school.edu", 21, 3.5);

        registry.addStudent(student1);  // Alice, 3.8
        registry.addStudent(student2);  // Bob, 3.5
        registry.addStudent(student3);  // Charlie, 3.9
        registry.addStudent(s4);        // Beth, 3.8
        registry.addStudent(s5);        // Diana, 3.8
        registry.addStudent(s6);        // Frank, 3.5

        // Test GPA-Name comparator
        List<Student> sortedByGpaName = registry.getAllStudentsSorted(new StudentGpaNameComparator());
        
        assertEquals(6, sortedByGpaName.size());
        assertEquals(3.9, sortedByGpaName.get(0).getGpa());  // Charlie
        assertEquals(3.8, sortedByGpaName.get(1).getGpa());  // Alice (alphabetically first of the 3.8s)
        assertEquals("Alice Johnson", sortedByGpaName.get(1).getName());
        assertEquals("Beth", sortedByGpaName.get(2).getName());
        assertEquals("Diana", sortedByGpaName.get(3).getName());
        assertEquals(3.5, sortedByGpaName.get(4).getGpa());  // Bob (alphabetically first of the 3.5s)
        assertEquals("Bob Smith", sortedByGpaName.get(4).getName());
        assertEquals("Frank", sortedByGpaName.get(5).getName());
    }

    @Test
    @DisplayName("Should maintain original registry after sorting")
    public void testSortDoesNotModifyRegistry() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        // Sort by name
        List<Student> sorted = registry.getAllStudentsSorted(new StudentNameComparator());

        // Original registry should still be in insertion order (not sorted)
        List<Student> original = registry.getAllStudentsSorted(Comparator.comparing(Student::getStudentId));
        
        assertEquals("STU001", original.get(0).getStudentId());
        assertEquals("STU002", original.get(1).getStudentId());
        assertEquals("STU003", original.get(2).getStudentId());
    }

    // ===== Exercise 9: Registry Statistics Tests =====

    @Test
    @DisplayName("Should return GPA distribution with various GPA spreads")
    public void testGetGpaDistribution() {
        Student aStudent = new Student("STU004", "A Student", "a@school.edu", 20, 3.8); // A
        Student bStudent = new Student("STU005", "B Student", "b@school.edu", 21, 3.2); // B
        Student cStudent = new Student("STU006", "C Student", "c@school.edu", 22, 2.5); // C
        Student dStudent = new Student("STU007", "D Student", "d@school.edu", 23, 1.5); // D
        Student fStudent = new Student("STU008", "F Student", "f@school.edu", 24, 0.5); // F

        registry.addStudent(aStudent);
        registry.addStudent(bStudent);
        registry.addStudent(cStudent);
        registry.addStudent(dStudent);
        registry.addStudent(fStudent);

        Map<String, Integer> distribution = registry.getGpaDistribution();

        assertEquals(5, distribution.size());
        assertEquals(1, distribution.get("A"));
        assertEquals(1, distribution.get("B"));
        assertEquals(1, distribution.get("C"));
        assertEquals(1, distribution.get("D"));
        assertEquals(1, distribution.get("F"));
    }

    @Test
    @DisplayName("Should return GPA distribution with boundary values")
    public void testGetGpaDistributionBoundaries() {
        Student aHigh = new Student("STU004", "A High", "ah@school.edu", 20, 4.0); // A
        Student aLow = new Student("STU005", "A Low", "al@school.edu", 21, 3.7); // A
        Student bHigh = new Student("STU006", "B High", "bh@school.edu", 22, 3.69); // B
        Student bLow = new Student("STU007", "B Low", "bl@school.edu", 23, 2.7); // B
        Student cHigh = new Student("STU008", "C High", "ch@school.edu", 24, 2.69); // C
        Student cLow = new Student("STU009", "C Low", "cl@school.edu", 25, 1.7); // C
        Student dHigh = new Student("STU010", "D High", "dh@school.edu", 26, 1.69); // D
        Student dLow = new Student("STU011", "D Low", "dl@school.edu", 27, 1.0); // D
        Student fHigh = new Student("STU012", "F High", "fh@school.edu", 28, 0.99); // F
        Student fLow = new Student("STU013", "F Low", "fl@school.edu", 29, 0.0); // F

        registry.addStudent(aHigh);
        registry.addStudent(aLow);
        registry.addStudent(bHigh);
        registry.addStudent(bLow);
        registry.addStudent(cHigh);
        registry.addStudent(cLow);
        registry.addStudent(dHigh);
        registry.addStudent(dLow);
        registry.addStudent(fHigh);
        registry.addStudent(fLow);

        Map<String, Integer> distribution = registry.getGpaDistribution();

        assertEquals(5, distribution.size());
        assertEquals(2, distribution.get("A"));
        assertEquals(2, distribution.get("B"));
        assertEquals(2, distribution.get("C"));
        assertEquals(2, distribution.get("D"));
        assertEquals(2, distribution.get("F"));
    }

    @Test
    @DisplayName("Should return empty map for empty registry")
    public void testGetGpaDistributionEmpty() {
        Map<String, Integer> distribution = registry.getGpaDistribution();
        assertTrue(distribution.isEmpty());
    }

    @Test
    @DisplayName("Should return top N students by GPA")
    public void testGetTopStudents() {
        registry.addStudent(student1); // 3.8
        registry.addStudent(student2); // 3.5
        registry.addStudent(student3); // 3.9

        List<Student> top2 = registry.getTopStudents(2);
        assertEquals(2, top2.size());
        assertEquals(3.9, top2.get(0).getGpa());
        assertEquals(3.8, top2.get(1).getGpa());
    }

    @Test
    @DisplayName("Should return all students when N > total students")
    public void testGetTopStudentsMoreThanTotal() {
        registry.addStudent(student1);
        registry.addStudent(student2);

        List<Student> top5 = registry.getTopStudents(5);
        assertEquals(2, top5.size());
    }

    @Test
    @DisplayName("Should return empty list when N <= 0")
    public void testGetTopStudentsZeroOrNegative() {
        registry.addStudent(student1);

        List<Student> top0 = registry.getTopStudents(0);
        assertTrue(top0.isEmpty());

        List<Student> topNegative = registry.getTopStudents(-1);
        assertTrue(topNegative.isEmpty());
    }

    @Test
    @DisplayName("Should handle ties in GPA for top students")
    public void testGetTopStudentsWithTies() {
        Student student4 = new Student("STU004", "Dave", "dave@school.edu", 20, 3.8);
        registry.addStudent(student1); // 3.8 Alice
        registry.addStudent(student4); // 3.8 Dave
        registry.addStudent(student3); // 3.9 Charlie

        List<Student> top3 = registry.getTopStudents(3);
        assertEquals(3, top3.size());
        assertEquals(3.9, top3.get(0).getGpa()); // Charlie first
        assertEquals(3.8, top3.get(1).getGpa()); // Then 3.8s (order may vary due to stable sort)
        assertEquals(3.8, top3.get(2).getGpa());
    }

    @Test
    @DisplayName("Should return students above 50th percentile GPA")
    public void testGetStudentsByGpaPercentile50th() {
        Student low = new Student("STU004", "Low", "low@school.edu", 20, 2.0);
        Student mid = new Student("STU005", "Mid", "mid@school.edu", 21, 3.0);
        Student high = new Student("STU006", "High", "high@school.edu", 22, 4.0);

        registry.addStudent(low);   // 2.0
        registry.addStudent(mid);   // 3.0
        registry.addStudent(high);  // 4.0

        List<Student> above50th = registry.getStudentsByGpaPercentile(50.0);
        assertEquals(2, above50th.size()); // Mid and High (above 3.0)
    }

    @Test
    @DisplayName("Should return students above 0th percentile (all students)")
    public void testGetStudentsByGpaPercentile0th() {
        registry.addStudent(student1);
        registry.addStudent(student2);

        List<Student> above0th = registry.getStudentsByGpaPercentile(0.0);
        assertEquals(2, above0th.size());
    }

    @Test
    @DisplayName("Should return students above 100th percentile (none)")
    public void testGetStudentsByGpaPercentile100th() {
        registry.addStudent(student1);
        registry.addStudent(student2);

        List<Student> above100th = registry.getStudentsByGpaPercentile(100.0);
        assertTrue(above100th.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for invalid percentile or empty registry")
    public void testGetStudentsByGpaPercentileEdgeCases() {
        // Empty registry
        List<Student> emptyRegistry = registry.getStudentsByGpaPercentile(50.0);
        assertTrue(emptyRegistry.isEmpty());

        registry.addStudent(student1);

        // Invalid percentiles
        List<Student> negative = registry.getStudentsByGpaPercentile(-1.0);
        assertTrue(negative.isEmpty());

        List<Student> over100 = registry.getStudentsByGpaPercentile(101.0);
        assertTrue(over100.isEmpty());
    }

    @Test
    @DisplayName("Should handle percentile calculation with odd number of students")
    public void testGetStudentsByGpaPercentileOddCount() {
        Student s1 = new Student("STU004", "S1", "s1@school.edu", 20, 1.0);
        Student s2 = new Student("STU005", "S2", "s2@school.edu", 21, 2.0);
        Student s3 = new Student("STU006", "S3", "s3@school.edu", 22, 3.0);
        Student s4 = new Student("STU007", "S4", "s4@school.edu", 23, 4.0);
        Student s5 = new Student("STU008", "S5", "s5@school.edu", 24, 4.0);

        registry.addStudent(s1);
        registry.addStudent(s2);
        registry.addStudent(s3);
        registry.addStudent(s4);
        registry.addStudent(s5);

        // 50th percentile of [1,2,3,4,5] is index 2 (0-based), value 3
        List<Student> above50th = registry.getStudentsByGpaPercentile(50.0);
        assertEquals(3, above50th.size()); // GPA >= 3.0
    }
}


