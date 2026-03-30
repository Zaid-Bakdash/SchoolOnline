package com.bootcamp.onlineschool.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher extends User {
    private String department;
    private List<Course> coursesTaught;

    public Teacher(String id, String name, String email, String department) {
        super(id, name, email);
        this.department = department;
        this.coursesTaught = new ArrayList<>();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void assignCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        boolean alreadyAssigned = coursesTaught.stream()
                .anyMatch(c -> c.getCourseId().equals(course.getCourseId()));
        if (alreadyAssigned) {
            throw new IllegalArgumentException("Course already assigned to teacher");
        }
        coursesTaught.add(course);
    }

    public boolean removeCourse(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            return false;
        }
        return coursesTaught.removeIf(c -> c.getCourseId().equals(courseId));
    }

    public List<Course> getCoursesTaught() {
        return new ArrayList<>(coursesTaught);
    }

    @Override
    public String getRole() {
        return "Teacher";
    }

    @Override
    public String toString() {
        return String.format("Teacher{id='%s', name='%s', email='%s', department='%s'}", 
                getId(), getName(), getEmail(), department);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(getId(), teacher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
