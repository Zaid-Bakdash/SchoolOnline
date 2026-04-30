package com.bootcamp.onlineschool.dto;

import com.bootcamp.onlineschool.entity.Teacher;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "Teacher data transfer object")
public class TeacherDTO {

    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "Unique teacher identifier", example = "T001", required = true)
    @NotBlank(message = "Teacher ID is required")
    @Size(max = 20, message = "Teacher ID must not exceed 20 characters")
    private String teacherId;

    @Schema(description = "Teacher's name", example = "Prof. Wilson", required = true)
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(description = "Teacher's email", example = "prof.wilson@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Schema(description = "Department where the teacher works", example = "Computer Science", required = true)
    @NotBlank(message = "Department is required")
    @Size(max = 50, message = "Department must not exceed 50 characters")
    private String department;

    @Schema(description = "Years of experience", example = "5", required = true)
    @NotNull(message = "Years of experience is required")
    private Integer yearsOfExperience;

    @Schema(description = "Teacher's salary", example = "75000.0", required = true)
    @NotNull(message = "Salary is required")
    private Double salary;

    @Schema(description = "Date when the teacher was hired", example = "2020-08-01", required = true)
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    // Default constructor
    public TeacherDTO() {}

    // Constructor with required fields
    public TeacherDTO(String teacherId, String name, String email, String department, Integer yearsOfExperience, Double salary, LocalDate hireDate) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.yearsOfExperience = yearsOfExperience;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    // Constructor with all fields
    public TeacherDTO(Long id, String teacherId, String name, String email, String department, Integer yearsOfExperience, Double salary, LocalDate hireDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.yearsOfExperience = yearsOfExperience;
        this.salary = salary;
        this.hireDate = hireDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
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

    // Utility method to convert from Entity to DTO
    public static TeacherDTO fromEntity(Teacher teacher) {
        if (teacher == null) {
            return null;
        }

        return new TeacherDTO(
            teacher.getId(),
            teacher.getTeacherId(),
            teacher.getName(),
            teacher.getEmail(),
            teacher.getDepartment(),
            teacher.getYearsOfExperience(),
            teacher.getSalary(),
            teacher.getHireDate(),
            teacher.getCreatedAt(),
            teacher.getUpdatedAt()
        );
    }

    // Utility method to convert from DTO to Entity (for updates)
    public Teacher toEntity() {
        Teacher teacher = new Teacher(
            this.teacherId,
            this.name,
            this.email,
            this.department,
            this.yearsOfExperience,
            this.salary,
            this.hireDate
        );
        teacher.setId(this.id);
        return teacher;
    }

    @Override
    public String toString() {
        return "TeacherDTO{" +
                "id=" + id +
                ", teacherId='" + teacherId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}