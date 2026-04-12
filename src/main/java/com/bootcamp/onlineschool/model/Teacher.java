package com.bootcamp.onlineschool.model;

import java.util.Objects;

/**
 * Teacher class demonstrating:
 * - Encapsulation (private fields, public getters/setters)
 * - Constructor overloading
 * - toString() method
 * - equals() and hashCode() methods
 */
public class Teacher {
    private String teacherId;
    private String name;
    private String email;
    private String department;
    private String specialization;

    // Constructor with required fields
    public Teacher(String teacherId, String name, String email, String department) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.specialization = "";
    }

    // Constructor with all fields
    public Teacher(String teacherId, String name, String email, String department, String specialization) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.specialization = specialization;
    }

    // Getters and Setters
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // Validate email format
    public boolean isValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    @Override
    public String toString() {
        return String.format("Teacher{id='%s', name='%s', email='%s', department='%s', specialization='%s'}",
                teacherId, name, email, department, specialization);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(teacherId, teacher.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId);
    }
}