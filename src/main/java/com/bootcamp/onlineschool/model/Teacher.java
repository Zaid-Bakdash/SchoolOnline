package com.bootcamp.onlineschool.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Teacher JPA Entity
 * Represents a teacher with one-to-many relationship to courses
 */
@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotNull
    @Size(max = 100)
    private String name;

    @Column(nullable = false, unique = true)
    @NotNull
    @Email
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer yearsOfExperience;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Double salary;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Course> courses = new HashSet<>();

    // Default constructor
    public Teacher() {}

    // Constructor with required fields
    public Teacher(String name, String email, Department department, Integer yearsOfExperience, Double salary) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.yearsOfExperience = yearsOfExperience;
        this.salary = salary;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    // Helper methods for relationship management
    public void addCourse(Course course) {
        this.courses.add(course);
        course.setTeacher(this);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.setTeacher(null);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Teacher{id=%d, name='%s', email='%s', departmentId=%s, yearsOfExperience=%d, salary=%.2f}",
                id, name, email, department != null ? department.getId() : null, yearsOfExperience, salary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(email, teacher.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
