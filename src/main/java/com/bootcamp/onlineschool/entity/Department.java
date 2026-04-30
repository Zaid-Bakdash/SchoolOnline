package com.bootcamp.onlineschool.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Department ID is required")
    @Size(max = 20, message = "Department ID must not exceed 20 characters")
    @Column(name = "department_id", nullable = false, unique = true, length = 20)
    private String departmentId;

    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Size(max = 100, message = "Department head name must not exceed 100 characters")
    @Column(length = 100)
    private String head;

    @NotNull(message = "Budget is required")
    @Column(nullable = false)
    private Double budget;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    @Column(length = 100)
    private String location;

    @Column(name = "established_date")
    private LocalDate establishedDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Department() {
    }

    public Department(String departmentId, String name, String head, Double budget, String location, LocalDate establishedDate) {
        this.departmentId = departmentId;
        this.name = name;
        this.head = head;
        this.budget = budget;
        this.location = location;
        this.establishedDate = establishedDate;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        if (budget == null || budget < 0) {
            throw new IllegalArgumentException("Budget must be non-negative");
        }
        this.budget = budget;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(LocalDate establishedDate) {
        this.establishedDate = establishedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isWithinBudget(Double amount) {
        if (amount == null) {
            return false;
        }
        return budget != null && budget >= amount;
    }

    public void increaseBudget(Double amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Increase amount must be non-negative");
        }
        setBudget(this.budget + amount);
    }

    public void decreaseBudget(Double amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Decrease amount must be non-negative");
        }
        if (budget - amount < 0) {
            throw new IllegalArgumentException("Budget cannot be reduced below zero");
        }
        setBudget(this.budget - amount);
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", departmentId='" + departmentId + '\'' +
                ", name='" + name + '\'' +
                ", head='" + head + '\'' +
                ", budget=" + budget +
                ", location='" + location + '\'' +
                ", establishedDate=" + establishedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(departmentId, that.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId);
    }
}
