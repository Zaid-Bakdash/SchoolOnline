package com.bootcamp.onlineschool.dto;

import jakarta.validation.constraints.*;

public class CourseDTO {
    @NotNull
    @NotBlank
    private String id;

    @NotNull
    @NotBlank
    private String courseName;

    @Min(0)
    private int credits;

    @NotNull
    @NotBlank
    private String instructor;

    @Min(0)
    private int maxStudents;

    private int enrolledStudents;

    public CourseDTO() {}

    public CourseDTO(String id, String courseName, int credits, String instructor, int maxStudents, int enrolledStudents) {
        this.id = id;
        this.courseName = courseName;
        this.credits = credits;
        this.instructor = instructor;
        this.maxStudents = maxStudents;
        this.enrolledStudents = enrolledStudents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(int enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public int getAvailableSeats() {
        return maxStudents - enrolledStudents;
    }
}
