package com.bootcamp.onlineschool.model;

import java.util.*;
import java.util.Objects;

public class Student extends User {
    private int age;
    private double gpa;
    private List<Course> enrolledCourses;

    // Constructor with required fields
    public Student(String studentId, String name, String email) {
        super(studentId, name, email);
        this.age = 0;
        this.gpa = 0.0;
        this.enrolledCourses = new ArrayList<>();
    }

    // Constructor with all fields
    public Student(String studentId, String name, String email, int age, double gpa) {
        super(studentId, name, email);
        setAge(age);  // Use setter for validation
        setGpa(gpa);  // Use setter for validation
        this.enrolledCourses = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getStudentId() {
        return getId();
    }

    public void setStudentId(String studentId) {
        setId(studentId);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 16 && age <= 100) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age must be between 16 and 100");
        }
    }

    public double getGpa() {
        return gpa;
    }
    
    public void setGpa(double gpa) {
        if (gpa >= 0.0 && gpa <= 4.0) {
            this.gpa = gpa;
        } else {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
    }
    
    // Validate email format
    public boolean isValidEmail() {
        String emailValue = getEmail();
        return emailValue != null && emailValue.contains("@") && emailValue.contains(".") && emailValue.endsWith("@school.edu");
    }
    
    /**
     * Enroll student in a course (prevents duplicate enrollment)
     */
    public void enrollInCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        // Check if already enrolled
        boolean alreadyEnrolled = enrolledCourses.stream()
                .anyMatch(c -> c.getCourseId().equals(course.getCourseId()));
        if (alreadyEnrolled) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }
        enrolledCourses.add(course);
    }
    
    /**
     * Drop a course by course ID
     */
    public boolean dropCourse(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            return false;
        }
        return enrolledCourses.removeIf(c -> c.getCourseId().equals(courseId));
    }
    
    /**
     * Get list of enrolled courses
     */
    public List<Course> getEnrolledCourses() {
        return new ArrayList<>(enrolledCourses);  // Return copy to prevent external modification
    }
    
    /**
     * Calculate total credits from all enrolled courses
     */
    public int getTotalCredits() {
        return enrolledCourses.stream()
                .mapToInt(Course::getCredits)
                .sum();
    }
    
    /**
     * Calculate weighted GPA based on grades and course credits
     * Grade mapping: A=4.0, B=3.0, C=2.0, D=1.0, F=0.0
     * Automatically updates the student's GPA field
     * Handles invalid grades gracefully
     */
    public void calculateGpa(Map<Course, String> grades) {
        if (grades == null || grades.isEmpty()) {
            return;  // No grades to calculate, GPA remains unchanged
        }
        
        double totalWeightedGrade = 0.0;
        int totalCredits = 0;
        
        for (Map.Entry<Course, String> entry : grades.entrySet()) {
            Course course = entry.getKey();
            String grade = entry.getValue();
            
            // Get grade point value
            double gradePoint = getGradePoint(grade);
            if (gradePoint < 0) {
                // Invalid grade, skip this entry
                continue;
            }
            
            // Calculate weighted grade
            int credits = course.getCredits();
            totalWeightedGrade += gradePoint * credits;
            totalCredits += credits;
        }
        
        // Calculate and set GPA
        if (totalCredits > 0) {
            double calculatedGpa = totalWeightedGrade / totalCredits;
            setGpa(calculatedGpa);
        }
    }
    
    /**
     * Helper method to convert letter grade to grade point
     * Returns -1 for invalid grades
     */
    private double getGradePoint(String grade) {
        if (grade == null) {
            return -1;
        }
        
        switch (grade.toUpperCase().trim()) {
            case "A":
                return 4.0;
            case "B":
                return 3.0;
            case "C":
                return 2.0;
            case "D":
                return 1.0;
            case "F":
                return 0.0;
            default:
                return -1;  // Invalid grade
        }
    }
    
    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', email='%s', age=%d, gpa=%.2f}",
                getId(), getName(), getEmail(), age, gpa);
    }

    @Override
    public String getRole() {
        return "Student";
    }
}
