package com.bootcamp.onlineschool.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Enrollment JPA Entity
 * Represents the many-to-many relationship between students and courses with additional attributes
 */
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull
    private Course course;

    @Column(name = "enrollment_date", nullable = false)
    @NotNull
    private LocalDate enrollmentDate;

    @Column(length = 2)
    @Size(max = 2)
    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private EnrollmentStatus status;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor
    public Enrollment() {}

    // Constructor with required fields
    public Enrollment(Student student, Course course, LocalDate enrollmentDate, EnrollmentStatus status) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
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

    // Business methods
    public boolean isCompleted() {
        return status == EnrollmentStatus.COMPLETED;
    }

    public boolean isActive() {
        return status == EnrollmentStatus.ENROLLED;
    }

    public void complete(String grade) {
        this.grade = grade;
        this.status = EnrollmentStatus.COMPLETED;
        this.completionDate = LocalDate.now();
    }

    public void drop() {
        this.status = EnrollmentStatus.DROPPED;
    }

    public void withdraw() {
        this.status = EnrollmentStatus.WITHDRAWN;
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
        return String.format("Enrollment{id=%d, studentId=%s, courseId=%s, status=%s, grade='%s', enrollmentDate=%s}",
                id,
                (student != null ? student.getId() : null),
                (course != null ? course.getId() : null),
                status,
                grade,
                enrollmentDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment)) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(student, that.student) &&
               Objects.equals(course, that.course) &&
               Objects.equals(enrollmentDate, that.enrollmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course, enrollmentDate);
    }
}