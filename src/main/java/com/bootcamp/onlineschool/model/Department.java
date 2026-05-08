package com.bootcamp.onlineschool.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Department JPA Entity
 */
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @NotNull
    @Size(max = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    @NotNull
    @Size(max = 10)
    private String code;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Double budget;

    @Column(length = 100)
    @Size(max = 100)
    private String location;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Course> courses = new HashSet<>();

    public Department() {
    }

    public Department(String name, String code, Double budget, String location) {
        this.name = name;
        this.code = code;
        this.budget = budget;
        this.location = location;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void addTeacher(Teacher teacher) {
        if (teacher == null) {
            return;
        }
        teachers.add(teacher);
        teacher.setDepartment(this);
    }

    public void removeTeacher(Teacher teacher) {
        if (teacher == null) {
            return;
        }
        teachers.remove(teacher);
        if (teacher.getDepartment() == this) {
            teacher.setDepartment(null);
        }
    }

    public void addCourse(Course course) {
        if (course == null) {
            return;
        }
        courses.add(course);
        course.setDepartment(this);
    }

    public void removeCourse(Course course) {
        if (course == null) {
            return;
        }
        courses.remove(course);
        if (course.getDepartment() == this) {
            course.setDepartment(null);
        }
    }

    public void increaseBudget(Double amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Budget increase must be positive");
        }
        this.budget += amount;
    }

    public void decreaseBudget(Double amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Budget decrease must be positive");
        }
        if (amount > this.budget) {
            throw new IllegalArgumentException("Budget cannot become negative");
        }
        this.budget -= amount;
    }

    public int getTeacherCount() {
        return teachers.size();
    }

    public int getCourseCount() {
        return courses.size();
    }

    @Override
    public String toString() {
        return String.format("Department{id=%d, name='%s', code='%s', budget=%.2f, location='%s'}",
                id, name, code, budget, location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
