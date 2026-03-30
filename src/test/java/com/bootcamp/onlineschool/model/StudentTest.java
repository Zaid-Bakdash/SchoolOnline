package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Student class
 * Demonstrates:
 * - Test setup with @BeforeEach
 * - Assertions (assertEquals, assertTrue, assertThrows, etc.)
 * - Test naming conventions
 * - Test organization
 */
@DisplayName("Student Class Tests")
public class StudentTest {
    
    private Student student;
    
    @BeforeEach
    public void setUp() {
        student = new Student("STU001", "John Doe", "john@school.edu");
    }
    
    @Test
    @DisplayName("Should create student with valid data")
    public void testStudentCreation() {
        assertNotNull(student);
        assertEquals("STU001", student.getStudentId());
        assertEquals("John Doe", student.getName());
        assertEquals("john@school.edu", student.getEmail());
        assertEquals(0.0, student.getGpa());
    }
    
    @Test
    @DisplayName("Should validate email format")
    public void testEmailValidation() {
        assertTrue(student.isValidEmail());
        
        Student invalidStudent = new Student("STU002", "Jane Doe", "invalid-email");
        assertFalse(invalidStudent.isValidEmail());
    }
    
    // ===== Exercise 2: Email Domain Validation Tests =====
    
    @Test
    @DisplayName("Should accept valid emails ending with @school.edu")
    public void testValidEmailsWithSchoolDomain() {
        Student student1 = new Student("STU002", "Jane Doe", "student@school.edu");
        assertTrue(student1.isValidEmail(), "Simple student email should be valid");
        
        Student student2 = new Student("STU003", "John Smith", "john.doe@school.edu");
        assertTrue(student2.isValidEmail(), "Email with dot in name should be valid");
        
        Student student3 = new Student("STU004", "Alice", "alice.johnson.smith@school.edu");
        assertTrue(student3.isValidEmail(), "Email with multiple dots in name should be valid");
    }
    
    @Test
    @DisplayName("Should reject emails from other domains")
    public void testInvalidEmailsFromOtherDomains() {
        Student gmailStudent = new Student("STU005", "Bob", "bob@gmail.com");
        assertFalse(gmailStudent.isValidEmail(), "Gmail address should be invalid");
        
        Student yahooStudent = new Student("STU006", "Charlie", "charlie@yahoo.com");
        assertFalse(yahooStudent.isValidEmail(), "Yahoo address should be invalid");
        
        Student schoolComStudent = new Student("STU007", "Diana", "diana@school.com");
        assertFalse(schoolComStudent.isValidEmail(), "school.com (not school.edu) should be invalid");
    }
    
    @Test
    @DisplayName("Should reject emails without @ or without .")
    public void testInvalidEmailFormatWithoutSpecialCharacters() {
        Student noAtSign = new Student("STU008", "Eve", "eve.schooledu");
        assertFalse(noAtSign.isValidEmail(), "Email without @ should be invalid");
        
        Student noDot = new Student("STU009", "Frank", "frankedu@school");
        assertFalse(noDot.isValidEmail(), "Email without . should be invalid");
        
        Student invalidFormat = new Student("STU010", "Grace", "invalid-email");
        assertFalse(invalidFormat.isValidEmail(), "Random string should be invalid");
    }
    
    @Test
    @DisplayName("Should reject null or empty emails")
    public void testNullAndEmptyEmails() {
        Student student1 = new Student("STU011", "Henry", "");
        assertFalse(student1.isValidEmail(), "Empty email should be invalid");
    }
    
    @Test
    @DisplayName("Should validate email when updating with setter")
    public void testEmailValidationAfterUpdate() {
        // Start with invalid email
        student.setEmail("invalid@gmail.com");
        assertFalse(student.isValidEmail(), "Gmail email should be invalid");
        
        // Update to valid email
        student.setEmail("valid.student@school.edu");
        assertTrue(student.isValidEmail(), "school.edu email should be valid");
        
        // Update to another invalid email
        student.setEmail("wrong@school.com");
        assertFalse(student.isValidEmail(), "school.com should be invalid");
    }

    
    @Test
    @DisplayName("Should set and get GPA correctly")
    public void testGpaSetterGetter() {
        student.setGpa(3.5);
        assertEquals(3.5, student.getGpa());
        
        student.setGpa(4.0);
        assertEquals(4.0, student.getGpa());
        
        student.setGpa(0.0);
        assertEquals(0.0, student.getGpa());
    }
    
    @Test
    @DisplayName("Should throw exception for invalid GPA")
    public void testInvalidGpa() {
        assertThrows(IllegalArgumentException.class, () -> student.setGpa(4.5));
        assertThrows(IllegalArgumentException.class, () -> student.setGpa(-1.0));
    }
    
    @Test
    @DisplayName("Should update student information")
    public void testUpdateStudentInfo() {
        student.setName("Jane Doe");
        student.setEmail("jane@school.edu");
        
        assertEquals("Jane Doe", student.getName());
        assertEquals("jane@school.edu", student.getEmail());
    }
    
    @Test
    @DisplayName("Should generate correct toString representation")
    public void testToString() {
        student.setGpa(3.8);
        String result = student.toString();
        
        assertTrue(result.contains("STU001"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("john@school.edu"));
        assertTrue(result.contains("3.80"));
    }
    
    @Test
    @DisplayName("Should compare students by ID")
    public void testEqualsAndHashCode() {
        Student student2 = new Student("STU001", "Different Name", "different@school.edu");
        Student student3 = new Student("STU002", "John Doe", "john@school.edu");
        
        assertEquals(student, student2);
        assertNotEquals(student, student3);
        assertEquals(student.hashCode(), student2.hashCode());
    }
    
    // ===== Exercise 1: Age Validation Tests =====
    
    @Test
    @DisplayName("Should set and get age correctly for valid values")
    public void testAgeSetterGetterValidValues() {
        student.setAge(16);  // Minimum valid age
        assertEquals(16, student.getAge());
        
        student.setAge(50);  // Typical age
        assertEquals(50, student.getAge());
        
        student.setAge(100); // Maximum valid age
        assertEquals(100, student.getAge());
    }
    
    @Test
    @DisplayName("Should throw exception for age below 16")
    public void testInvalidAgeBelowMinimum() {
        assertThrows(IllegalArgumentException.class, () -> student.setAge(15));
        assertThrows(IllegalArgumentException.class, () -> student.setAge(0));
        assertThrows(IllegalArgumentException.class, () -> student.setAge(-5));
    }
    
    @Test
    @DisplayName("Should throw exception for age above 100")
    public void testInvalidAgeAboveMaximum() {
        assertThrows(IllegalArgumentException.class, () -> student.setAge(101));
        assertThrows(IllegalArgumentException.class, () -> student.setAge(150));
    }
    
    @Test
    @DisplayName("Should include age in toString representation")
    public void testAgeInToString() {
        student.setAge(25);
        String result = student.toString();
        
        assertTrue(result.contains("age=25"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("STU001"));
    }
    
    @Test
    @DisplayName("Should create student with age through full constructor")
    public void testStudentCreationWithAge() {
        Student studentWithAge = new Student("STU003", "Alice Smith", "alice@school.edu", 20, 3.7);
        
        assertEquals(20, studentWithAge.getAge());
        assertEquals("STU003", studentWithAge.getStudentId());
        assertEquals(3.7, studentWithAge.getGpa());
    }
    
    @Test
    @DisplayName("Should throw exception when creating student with invalid age in constructor")
    public void testConstructorWithInvalidAge() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Student("STU004", "Bob", "bob@school.edu", 10, 3.5));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Student("STU005", "Charlie", "charlie@school.edu", 105, 3.5));
    }
    
    // ===== Exercise 4: Course Enrollment Tests =====
    
    @Test
    @DisplayName("Should enroll student in a course")
    public void testEnrollInCourse() {
        Course course = new Course("CS101", "Introduction to Java", 3);
        student.enrollInCourse(course);
        
        assertEquals(1, student.getEnrolledCourses().size());
        assertEquals("CS101", student.getEnrolledCourses().get(0).getCourseId());
    }
    
    @Test
    @DisplayName("Should enroll student in multiple courses")
    public void testEnrollInMultipleCourses() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 4);
        Course course3 = new Course("CS103", "Web Development", 3);
        
        student.enrollInCourse(course1);
        student.enrollInCourse(course2);
        student.enrollInCourse(course3);
        
        assertEquals(3, student.getEnrolledCourses().size());
    }
    
    @Test
    @DisplayName("Should prevent duplicate course enrollment")
    public void testPreventDuplicateEnrollment() {
        Course course = new Course("CS101", "Introduction to Java", 3);
        
        student.enrollInCourse(course);
        assertThrows(IllegalArgumentException.class, () -> student.enrollInCourse(course),
                "Should throw exception when enrolling in same course twice");
    }
    
    @Test
    @DisplayName("Should throw exception when enrolling null course")
    public void testEnrollNullCourse() {
        assertThrows(IllegalArgumentException.class, () -> student.enrollInCourse(null));
    }
    
    @Test
    @DisplayName("Should drop a course by ID")
    public void testDropCourse() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 4);
        
        student.enrollInCourse(course1);
        student.enrollInCourse(course2);
        assertEquals(2, student.getEnrolledCourses().size());
        
        boolean dropped = student.dropCourse("CS101");
        assertTrue(dropped);
        assertEquals(1, student.getEnrolledCourses().size());
        assertEquals("CS102", student.getEnrolledCourses().get(0).getCourseId());
    }
    
    @Test
    @DisplayName("Should return false when dropping non-existent course")
    public void testDropNonExistentCourse() {
        Course course = new Course("CS101", "Introduction to Java", 3);
        student.enrollInCourse(course);
        
        boolean dropped = student.dropCourse("CS999");
        assertFalse(dropped);
        assertEquals(1, student.getEnrolledCourses().size());
    }
    
    @Test
    @DisplayName("Should handle null course ID when dropping")
    public void testDropCourseWithNullId() {
        Course course = new Course("CS101", "Introduction to Java", 3);
        student.enrollInCourse(course);
        
        boolean dropped = student.dropCourse(null);
        assertFalse(dropped);
        assertEquals(1, student.getEnrolledCourses().size());
    }
    
    @Test
    @DisplayName("Should handle empty course ID when dropping")
    public void testDropCourseWithEmptyId() {
        Course course = new Course("CS101", "Introduction to Java", 3);
        student.enrollInCourse(course);
        
        boolean dropped = student.dropCourse("");
        assertFalse(dropped);
        assertEquals(1, student.getEnrolledCourses().size());
    }
    
    @Test
    @DisplayName("Should return copy of enrolled courses list")
    public void testGetEnrolledCoursesReturnsCopy() {
        Course course = new Course("CS101", "Introduction to Java", 3);
        student.enrollInCourse(course);
        
        var courseList = student.getEnrolledCourses();
        courseList.clear();  // Modify the returned list
        
        // Original list should not be affected
        assertEquals(1, student.getEnrolledCourses().size());
    }
    
    @Test
    @DisplayName("Should calculate total credits correctly")
    public void testGetTotalCredits() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 4);
        Course course3 = new Course("CS103", "Web Development", 3);
        
        assertEquals(0, student.getTotalCredits());
        
        student.enrollInCourse(course1);
        assertEquals(3, student.getTotalCredits());
        
        student.enrollInCourse(course2);
        assertEquals(7, student.getTotalCredits());
        
        student.enrollInCourse(course3);
        assertEquals(10, student.getTotalCredits());
    }
    
    @Test
    @DisplayName("Should update total credits after dropping course")
    public void testTotalCreditsAfterDrop() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 4);
        
        student.enrollInCourse(course1);
        student.enrollInCourse(course2);
        assertEquals(7, student.getTotalCredits());
        
        student.dropCourse("CS102");
        assertEquals(3, student.getTotalCredits());
    }
    
    @Test
    @DisplayName("Should handle total credits with 0 credit courses")
    public void testTotalCreditsWithZeroCredit() {
        Course course = new Course("CS101", "Seminar", 0);
        student.enrollInCourse(course);
        
        assertEquals(0, student.getTotalCredits());
    }
    
    // ===== Exercise 5: GPA Calculator Tests =====
    
    @Test
    @DisplayName("Should calculate perfect GPA with all A grades")
    public void testCalculatePerfectGpa() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 4);
        Course course3 = new Course("CS103", "Web Development", 3);
        
        var grades = new java.util.HashMap<Course, String>();
        grades.put(course1, "A");
        grades.put(course2, "A");
        grades.put(course3, "A");
        
        student.calculateGpa(grades);
        assertEquals(4.0, student.getGpa(), 0.01);
    }
    
    @Test
    @DisplayName("Should calculate GPA with mixed grades")
    public void testCalculateGpaWithMixedGrades() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 4);
        Course course3 = new Course("CS103", "Web Development", 3);
        
        var grades = new java.util.HashMap<Course, String>();
        grades.put(course1, "A");  // 4.0 * 3 = 12
        grades.put(course2, "B");  // 3.0 * 4 = 12
        grades.put(course3, "C");  // 2.0 * 3 = 6
        
        // Total: (12 + 12 + 6) / (3 + 4 + 3) = 30 / 10 = 3.0
        student.calculateGpa(grades);
        assertEquals(3.0, student.getGpa(), 0.01);
    }
    
    @Test
    @DisplayName("Should calculate GPA with different grade values")
    public void testCalculateGpaWithDifferentGradeValues() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 2);
        Course course3 = new Course("CS103", "Web Development", 4);
        Course course4 = new Course("CS104", "Database", 3);
        
        var grades = new java.util.HashMap<Course, String>();
        grades.put(course1, "A");  // 4.0 * 3 = 12
        grades.put(course2, "D");  // 1.0 * 2 = 2
        grades.put(course3, "B");  // 3.0 * 4 = 12
        grades.put(course4, "F");  // 0.0 * 3 = 0
        
        // Total: (12 + 2 + 12 + 0) / (3 + 2 + 4 + 3) = 26 / 12 ≈ 2.17
        student.calculateGpa(grades);
        assertEquals(2.17, student.getGpa(), 0.01);
    }
    
    @Test
    @DisplayName("Should handle case-insensitive grades")
    public void testCalculateGpaWithCaseInsensitiveGrades() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 3);
        
        var grades = new java.util.HashMap<Course, String>();
        grades.put(course1, "a");  // Lowercase 'a' should be treated as 'A'
        grades.put(course2, "B");
        
        // (4.0 * 3 + 3.0 * 3) / (3 + 3) = 21 / 6 = 3.5
        student.calculateGpa(grades);
        assertEquals(3.5, student.getGpa(), 0.01);
    }
    
    @Test
    @DisplayName("Should skip invalid grades and calculate with valid ones")
    public void testCalculateGpaWithInvalidGrades() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 4);
        Course course3 = new Course("CS103", "Web Development", 3);
        
        var grades = new java.util.HashMap<Course, String>();
        grades.put(course1, "A");     // Valid: 4.0 * 3 = 12
        grades.put(course2, "X");    // Invalid: skip
        grades.put(course3, "B");    // Valid: 3.0 * 3 = 9
        
        // Total: (12 + 9) / (3 + 3) = 21 / 6 = 3.5
        student.calculateGpa(grades);
        assertEquals(3.5, student.getGpa(), 0.01);
    }
    
    @Test
    @DisplayName("Should handle null grades in map and skip them")
    public void testCalculateGpaWithNullGradeInMap() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 4);
        
        var grades = new java.util.HashMap<Course, String>();
        grades.put(course1, "A");     // Valid: 4.0 * 3 = 12
        grades.put(course2, null);   // Invalid: skip
        
        // Total: (12) / (3) = 4.0
        student.calculateGpa(grades);
        assertEquals(4.0, student.getGpa(), 0.01);
    }
    
    @Test
    @DisplayName("Should not update GPA if grades map is empty")
    public void testCalculateGpaWithEmptyGradesMap() {
        student.setGpa(3.5);
        
        var grades = new java.util.HashMap<Course, String>();
        student.calculateGpa(grades);
        
        // GPA should remain unchanged
        assertEquals(3.5, student.getGpa());
    }
    
    @Test
    @DisplayName("Should not update GPA if grades map is null")
    public void testCalculateGpaWithNullGradesMap() {
        student.setGpa(3.5);
        
        student.calculateGpa(null);
        
        // GPA should remain unchanged
        assertEquals(3.5, student.getGpa());
    }
    
    @Test
    @DisplayName("Should handle courses with different credit values")
    public void testCalculateGpaWithVariedCredits() {
        Course course1 = new Course("CS101", "Intro", 1);
        Course course2 = new Course("CS102", "Intermediate", 5);
        Course course3 = new Course("CS103", "Advanced", 2);
        
        var grades = new java.util.HashMap<Course, String>();
        grades.put(course1, "A");    // 4.0 * 1 = 4
        grades.put(course2, "B");    // 3.0 * 5 = 15
        grades.put(course3, "C");    // 2.0 * 2 = 4
        
        // Total: (4 + 15 + 4) / (1 + 5 + 2) = 23 / 8 = 2.875
        student.calculateGpa(grades);
        assertEquals(2.875, student.getGpa(), 0.01);
    }
    
    @Test
    @DisplayName("Should calculate GPA with all failing grades")
    public void testCalculateGpaWithAllFailingGrades() {
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 4);
        
        var grades = new java.util.HashMap<Course, String>();
        grades.put(course1, "F");    // 0.0 * 3 = 0
        grades.put(course2, "F");    // 0.0 * 4 = 0
        
        // Total: (0 + 0) / (3 + 4) = 0 / 7 = 0.0
        student.calculateGpa(grades);
        assertEquals(0.0, student.getGpa(), 0.01);
    }
    
    @Test
    @DisplayName("Should update existing GPA with new calculation")
    public void testCalculateGpaUpdatesExistingGpa() {
        student.setGpa(2.5);
        assertEquals(2.5, student.getGpa());
        
        Course course1 = new Course("CS101", "Introduction to Java", 3);
        Course course2 = new Course("CS102", "Data Structures", 4);
        
        var grades = new java.util.HashMap<Course, String>();
        grades.put(course1, "A");    // 4.0 * 3 = 12
        grades.put(course2, "A");    // 4.0 * 4 = 16
        
        // Total: (12 + 16) / (3 + 4) = 28 / 7 = 4.0
        student.calculateGpa(grades);
        assertEquals(4.0, student.getGpa(), 0.01);
    }

    @Test
    @DisplayName("Should support user polymorphism for Student and Teacher")
    public void testUserPolymorphism() {
        User studentUser = new Student("STU100", "Polly", "polly@school.edu");
        User teacherUser = new Teacher("TCH100", "Mr. Smith", "smith@school.edu", "Math");

        assertEquals("Student", studentUser.getRole());
        assertEquals("Teacher", teacherUser.getRole());

        assertNotEquals(studentUser, teacherUser);
        assertEquals("Polly", studentUser.getName());
        assertEquals("Math", ((Teacher) teacherUser).getDepartment());
    }

    @Test
    @DisplayName("Should assign and remove courses for Teacher")
    public void testTeacherCourseAssignment() {
        Teacher teacher = new Teacher("TCH101", "Ms. Jane", "jane@school.edu", "Science");
        Course course1 = new Course("BIO101", "Biology", 3);
        Course course2 = new Course("CHEM101", "Chemistry", 4);

        teacher.assignCourse(course1);
        teacher.assignCourse(course2);

        assertEquals(2, teacher.getCoursesTaught().size());

        assertTrue(teacher.removeCourse("BIO101"));
        assertEquals(1, teacher.getCoursesTaught().size());

        assertFalse(teacher.removeCourse("NONEXISTENT"));
    }

    @Test
    @DisplayName("Should prevent duplicate course assignment for Teacher")
    public void testTeacherDuplicateCourseAssignment() {
        Teacher teacher = new Teacher("TCH102", "Dr. New", "new@school.edu", "History");
        Course course = new Course("HIS101", "World History", 3);

        teacher.assignCourse(course);
        assertThrows(IllegalArgumentException.class, () -> teacher.assignCourse(course));
    }

    @Test
    @DisplayName("Should return copy of courses taught list")
    public void testTeacherCoursesReturnedListIsCopy() {
        Teacher teacher = new Teacher("TCH103", "Ms. Echo", "echo@school.edu", "English");
        Course course = new Course("ENG101", "English Literature", 3);

        teacher.assignCourse(course);
        var courses = teacher.getCoursesTaught();
        courses.clear();

        assertEquals(1, teacher.getCoursesTaught().size());
    }
}


