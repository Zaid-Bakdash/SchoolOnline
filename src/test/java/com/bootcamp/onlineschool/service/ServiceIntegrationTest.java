package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.StudentRegistry;
import com.bootcamp.onlineschool.TeacherRegistry;
import com.bootcamp.onlineschool.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests demonstrating interaction between multiple services.
 * Tests complete academic workflows and cross-service operations.
 */
@SpringBootTest
@DisplayName("Service Integration Tests")
public class ServiceIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRegistry studentRegistry;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRegistry teacherRegistry;

    @Autowired
    private DepartmentService departmentService;

    @BeforeEach
    public void setUp() {
        // Clear all data before each test
        studentRegistry.clear();
        courseService.clear();
        teacherRegistry.clear();
        departmentService.clear();
    }

    @Test
    @DisplayName("Should create complete academic structure: Department -> Teachers -> Courses -> Students")
    public void testCompleteAcademicStructure() {
        // Create department
        Department csDept = departmentService.createDepartment("CS", "Computer Science", "Dr. Smith", 150000.0);
        assertNotNull(csDept);
        assertEquals("CS", csDept.getId());

        // Create teachers
        Teacher teacher1 = new Teacher("T001", "Dr. Alice Johnson", "alice@school.edu", "Computer Science");
        Teacher teacher2 = new Teacher("T002", "Dr. Bob Wilson", "bob@school.edu", "Computer Science");
        teacherService.addTeacher(teacher1);
        teacherService.addTeacher(teacher2);

        // Assign teachers to department
        departmentService.assignTeacherToDepartment("CS", "T001");
        departmentService.assignTeacherToDepartment("CS", "T002");

        // Create courses
        Course javaCourse = courseService.createCourse("CS101", "Introduction to Java", 3, "Dr. Alice Johnson", 30);
        Course pythonCourse = courseService.createCourse("CS102", "Python Programming", 3, "Dr. Bob Wilson", 25);

        // Create students
        Student student1 = new Student("S001", "John Doe", "john@school.edu", 3.8);
        Student student2 = new Student("S002", "Jane Smith", "jane@school.edu", 3.9);
        studentService.addStudent(student1);
        studentService.addStudent(student2);

        // Enroll students in courses
        assertTrue(courseService.enrollStudent("CS101"));
        assertTrue(courseService.enrollStudent("CS101"));
        assertTrue(courseService.enrollStudent("CS102"));

        // Verify complete structure
        assertEquals(1, departmentService.getDepartmentCount());
        assertEquals(2, teacherService.getTotalTeachers());
        assertEquals(2, courseService.getTotalCourses());
        assertEquals(2, studentService.getTotalStudents());

        // Verify department has teachers
        Department dept = departmentService.getDepartmentById("CS");
        assertEquals(2, dept.getTeacherCount());

        // Verify courses have enrollments
        Course course1 = courseService.getCourseById("CS101");
        Course course2 = courseService.getCourseById("CS102");
        assertEquals(2, course1.getEnrolledStudents());
        assertEquals(1, course2.getEnrolledStudents());
    }

    @Test
    @DisplayName("Should find all teachers in a department")
    public void testFindTeachersInDepartment() {
        // Setup department and teachers
        departmentService.createDepartment("MATH", "Mathematics", "Dr. Brown", 120000.0);

        Teacher mathTeacher1 = new Teacher("T003", "Dr. Charlie Brown", "charlie@school.edu", "Mathematics");
        Teacher mathTeacher2 = new Teacher("T004", "Dr. Diana White", "diana@school.edu", "Mathematics");
        Teacher csTeacher = new Teacher("T005", "Dr. Eve Black", "eve@school.edu", "Computer Science");

        teacherService.addTeacher(mathTeacher1);
        teacherService.addTeacher(mathTeacher2);
        teacherService.addTeacher(csTeacher);

        departmentService.assignTeacherToDepartment("MATH", "T003");
        departmentService.assignTeacherToDepartment("MATH", "T004");

        // Test cross-service query: find teachers in department
        List<Teacher> mathTeachers = teacherService.getTeachersByDepartment("Mathematics");
        assertEquals(2, mathTeachers.size());
        assertTrue(mathTeachers.stream().anyMatch(t -> t.getTeacherId().equals("T003")));
        assertTrue(mathTeachers.stream().anyMatch(t -> t.getTeacherId().equals("T004")));

        // Verify department has correct teacher count
        Department mathDept = departmentService.getDepartmentById("MATH");
        assertEquals(2, mathDept.getTeacherCount());
    }

    @Test
    @DisplayName("Should find all courses taught by a specific teacher")
    public void testFindCoursesByTeacher() {
        // Setup teacher and courses
        Teacher teacher = new Teacher("T006", "Dr. Frank Miller", "frank@school.edu", "Physics");
        teacherService.addTeacher(teacher);

        courseService.createCourse("PHY101", "Physics I", 4, "Dr. Frank Miller", 40);
        courseService.createCourse("PHY102", "Physics II", 4, "Dr. Frank Miller", 35);
        courseService.createCourse("CHEM101", "Chemistry I", 3, "Dr. Green", 30);

        // Test finding courses by teacher
        List<Course> allCourses = courseService.getAllCourses();
        List<Course> frankCourses = allCourses.stream()
                .filter(c -> "Dr. Frank Miller".equals(c.getInstructor()))
                .toList();

        assertEquals(2, frankCourses.size());
        assertTrue(frankCourses.stream().anyMatch(c -> c.getCourseId().equals("PHY101")));
        assertTrue(frankCourses.stream().anyMatch(c -> c.getCourseId().equals("PHY102")));
    }

    @Test
    @DisplayName("Should find all students in courses of a department")
    public void testFindStudentsInDepartmentCourses() {
        // Setup department, teachers, courses, and students
        departmentService.createDepartment("BIO", "Biology", "Dr. Plant", 100000.0);

        Teacher bioTeacher = new Teacher("T007", "Dr. Plant", "plant@school.edu", "Biology");
        teacherService.addTeacher(bioTeacher);
        departmentService.assignTeacherToDepartment("BIO", "T007");

        courseService.createCourse("BIO101", "Biology I", 3, "Dr. Plant", 50);
        courseService.createCourse("BIO102", "Biology II", 3, "Dr. Plant", 45);

        Student student1 = new Student("S003", "Alice Green", "alice@school.edu", 3.7);
        Student student2 = new Student("S004", "Bob Blue", "bob@school.edu", 3.5);
        Student student3 = new Student("S005", "Carol Yellow", "carol@school.edu", 3.6);
        studentService.addStudent(student1);
        studentService.addStudent(student2);
        studentService.addStudent(student3);

        // Enroll students in biology courses
        courseService.enrollStudent("BIO101");
        courseService.enrollStudent("BIO101");
        courseService.enrollStudent("BIO102");

        // Verify enrollments
        Course bio1 = courseService.getCourseById("BIO101");
        Course bio2 = courseService.getCourseById("BIO102");
        assertEquals(2, bio1.getEnrolledStudents());
        assertEquals(1, bio2.getEnrolledStudents());

        // Test that we can query students (though we don't have a direct method,
        // we can verify through course enrollments)
        List<Course> bioCourses = courseService.getAllCourses().stream()
                .filter(c -> "Dr. Plant".equals(c.getInstructor()))
                .toList();

        int totalEnrolledInBio = bioCourses.stream()
                .mapToInt(Course::getEnrolledStudents)
                .sum();

        assertEquals(3, totalEnrolledInBio);
    }

    @Test
    @DisplayName("Should handle cascading operations when removing teacher")
    public void testRemoveTeacherCascading() {
        // Setup teacher and courses
        Teacher teacher = new Teacher("T008", "Dr. Grace Lee", "grace@school.edu", "Chemistry");
        teacherService.addTeacher(teacher);

        courseService.createCourse("CHEM101", "Chemistry I", 3, "Dr. Grace Lee", 40);
        courseService.createCourse("CHEM102", "Chemistry II", 3, "Dr. Grace Lee", 35);

        // Enroll students
        courseService.enrollStudent("CHEM101");
        courseService.enrollStudent("CHEM102");

        // Remove teacher
        boolean removed = teacherService.removeTeacher("T008");
        assertTrue(removed);

        // Verify teacher is gone
        assertEquals(0, teacherService.getTotalTeachers());

        // Courses should still exist but instructor might be affected
        // (depending on implementation - courses might keep the old instructor name)
        assertEquals(2, courseService.getTotalCourses());

        Course chem1 = courseService.getCourseById("CHEM101");
        Course chem2 = courseService.getCourseById("CHEM102");

        // Enrollments should be preserved
        assertEquals(1, chem1.getEnrolledStudents());
        assertEquals(1, chem2.getEnrolledStudents());
    }

    @Test
    @DisplayName("Should handle cascading operations when removing department")
    public void testRemoveDepartmentCascading() {
        // Setup department with teachers
        departmentService.createDepartment("ENG", "English", "Dr. Words", 80000.0);

        Teacher engTeacher1 = new Teacher("T009", "Dr. Words", "words@school.edu", "English");
        Teacher engTeacher2 = new Teacher("T010", "Dr. Prose", "prose@school.edu", "English");
        teacherService.addTeacher(engTeacher1);
        teacherService.addTeacher(engTeacher2);

        departmentService.assignTeacherToDepartment("ENG", "T009");
        departmentService.assignTeacherToDepartment("ENG", "T010");

        // Delete department
        departmentService.deleteDepartment("ENG");

        // Verify department is gone
        assertEquals(0, departmentService.getDepartmentCount());

        // Teachers should still exist (department removal doesn't cascade to teachers)
        assertEquals(2, teacherService.getTotalTeachers());

        // Teachers should still have their department information
        List<Teacher> engTeachers = teacherService.getTeachersByDepartment("English");
        assertEquals(2, engTeachers.size());
    }

    @Test
    @DisplayName("Should verify data consistency across services")
    public void testDataConsistency() {
        // Setup complete academic structure
        departmentService.createDepartment("PHYS", "Physics", "Dr. Newton", 140000.0);

        Teacher physTeacher1 = new Teacher("T011", "Dr. Newton", "newton@school.edu", "Physics");
        Teacher physTeacher2 = new Teacher("T012", "Dr. Einstein", "einstein@school.edu", "Physics");
        teacherService.addTeacher(physTeacher1);
        teacherService.addTeacher(physTeacher2);

        departmentService.assignTeacherToDepartment("PHYS", "T011");
        departmentService.assignTeacherToDepartment("PHYS", "T012");

        courseService.createCourse("PHYS101", "Physics I", 4, "Dr. Newton", 50);
        courseService.createCourse("PHYS102", "Physics II", 4, "Dr. Einstein", 45);

        Student student1 = new Student("S006", "Dave Physics", "dave@school.edu", 3.4);
        Student student2 = new Student("S007", "Eva Quantum", "eva@school.edu", 3.8);
        studentService.addStudent(student1);
        studentService.addStudent(student2);

        courseService.enrollStudent("PHYS101");
        courseService.enrollStudent("PHYS101");
        courseService.enrollStudent("PHYS102");

        // Verify counts across all services
        assertEquals(1, departmentService.getDepartmentCount());
        assertEquals(2, teacherService.getTotalTeachers());
        assertEquals(2, courseService.getTotalCourses());
        assertEquals(2, studentService.getTotalStudents());

        // Verify department-teacher relationship
        Department physDept = departmentService.getDepartmentById("PHYS");
        assertEquals(2, physDept.getTeacherCount());

        // Verify teacher-course relationship
        List<Course> newtonCourses = courseService.getAllCourses().stream()
                .filter(c -> "Dr. Newton".equals(c.getInstructor()))
                .toList();
        assertEquals(1, newtonCourses.size());

        // Verify total enrollments
        int totalEnrolled = courseService.getAllCourses().stream()
                .mapToInt(Course::getEnrolledStudents)
                .sum();
        assertEquals(3, totalEnrolled);

        // Verify average GPA calculation
        double avgGpa = studentService.getAverageGpa();
        assertEquals(3.6, avgGpa, 0.01);
    }

    @Test
    @DisplayName("Should handle exception scenarios in multi-service operations")
    public void testExceptionHandlingInMultiServiceOperations() {
        // Test trying to assign teacher to non-existent department
        Teacher teacher = new Teacher("T013", "Dr. Test", "test@school.edu", "Test");
        teacherService.addTeacher(teacher);

        assertThrows(DepartmentService.DepartmentNotFoundException.class,
            () -> departmentService.assignTeacherToDepartment("NONEXISTENT", "T013"));

        // Test trying to enroll in non-existent course
        assertThrows(CourseService.CourseNotFoundException.class,
            () -> courseService.enrollStudent("NONEXISTENT"));

        // Test trying to find non-existent student
        assertThrows(StudentService.StudentNotFoundException.class,
            () -> studentService.findStudentById("NONEXISTENT"));

        // Test creating duplicate department
        departmentService.createDepartment("TEST", "Test Department", "Dr. Test", 50000.0);
        assertThrows(DepartmentService.DepartmentAlreadyExistsException.class,
            () -> departmentService.createDepartment("TEST", "Another Test", "Dr. Another", 60000.0));

        // Test creating duplicate course
        courseService.createCourse("TEST101", "Test Course", 3, "Dr. Test", 30);
        assertThrows(CourseService.CourseAlreadyExistsException.class,
            () -> courseService.createCourse("TEST101", "Another Test", 2, "Dr. Another", 25));
    }

    @Test
    @DisplayName("Should maintain state consistency after complex operations")
    public void testStateConsistencyAfterComplexOperations() {
        // Setup complex academic structure
        departmentService.createDepartment("COMP", "Computer Science", "Dr. Code", 200000.0);
        departmentService.createDepartment("MATH", "Mathematics", "Dr. Calc", 150000.0);

        // Add teachers
        Teacher codeTeacher = new Teacher("T014", "Dr. Code", "code@school.edu", "Computer Science");
        Teacher calcTeacher = new Teacher("T015", "Dr. Calc", "calc@school.edu", "Mathematics");
        Teacher sharedTeacher = new Teacher("T016", "Dr. Both", "both@school.edu", "Computer Science");
        teacherService.addTeacher(codeTeacher);
        teacherService.addTeacher(calcTeacher);
        teacherService.addTeacher(sharedTeacher);

        // Assign teachers to departments
        departmentService.assignTeacherToDepartment("COMP", "T014");
        departmentService.assignTeacherToDepartment("COMP", "T016");
        departmentService.assignTeacherToDepartment("MATH", "T015");

        // Create courses
        courseService.createCourse("COMP101", "Programming", 3, "Dr. Code", 40);
        courseService.createCourse("COMP102", "Algorithms", 4, "Dr. Both", 35);
        courseService.createCourse("MATH101", "Calculus", 4, "Dr. Calc", 45);

        // Add students
        Student student1 = new Student("S008", "Programmer Kid", "kid@school.edu", 3.9);
        Student student2 = new Student("S009", "Math Whiz", "whiz@school.edu", 4.0);
        studentService.addStudent(student1);
        studentService.addStudent(student2);

        // Complex enrollments
        courseService.enrollStudent("COMP101");
        courseService.enrollStudent("COMP102");
        courseService.enrollStudent("MATH101");
        courseService.enrollStudent("MATH101");

        // Verify initial state
        assertEquals(2, departmentService.getDepartmentCount());
        assertEquals(3, teacherService.getTotalTeachers());
        assertEquals(3, courseService.getTotalCourses());
        assertEquals(2, studentService.getTotalStudents());

        // Perform complex operations: remove shared teacher from one department
        departmentService.removeTeacherFromDepartment("COMP", "T016");

        // Verify state consistency
        Department compDept = departmentService.getDepartmentById("COMP");
        assertEquals(1, compDept.getTeacherCount());
        assertTrue(compDept.getTeacherIds().contains("T014"));
        assertFalse(compDept.getTeacherIds().contains("T016"));

        // Teacher should still exist
        assertEquals(3, teacherService.getTotalTeachers());

        // Courses should still exist
        assertEquals(3, courseService.getTotalCourses());

        // Enrollments should be preserved
        Course comp102 = courseService.getCourseById("COMP102");
        assertEquals(1, comp102.getEnrolledStudents()); // Still has enrollment even though teacher was removed from dept
    }
}