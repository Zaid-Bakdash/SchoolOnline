package com.bootcamp.onlineschool.model;

import java.util.Objects;

public class Course {
    
    private String courseId;
    private String name;
    private int credits;
    
    public Course(String courseId, String name, int credits) {
        this.courseId = courseId;
        this.name = name;
        this.credits = credits;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    @Override
    public String toString() {
        return String.format("Course{id='%s', name='%s', credits=%d}", 
                courseId, name, credits);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}
