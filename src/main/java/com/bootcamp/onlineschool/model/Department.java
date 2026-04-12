package com.bootcamp.onlineschool.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Department class representing a department in the online school.
 * Contains department information and manages associated teachers.
 */
public class Department {
    private String id;
    private String name;
    private String head; // teacher name
    private double budget;
    private List<String> teacherIds;

    // Constructor with validation
    public Department(String id, String name, String head, double budget) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Department ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }
        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }

        this.id = id;
        this.name = name;
        this.head = head;
        this.budget = budget;
        this.teacherIds = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public List<String> getTeacherIds() {
        return new ArrayList<>(teacherIds); // Return defensive copy
    }

    public void setTeacherIds(List<String> teacherIds) {
        this.teacherIds = teacherIds != null ? new ArrayList<>(teacherIds) : new ArrayList<>();
    }

    // Methods
    public void addTeacherId(String teacherId) {
        if (teacherId != null && !teacherId.trim().isEmpty() && !teacherIds.contains(teacherId)) {
            teacherIds.add(teacherId);
        }
    }

    public void removeTeacherId(String teacherId) {
        teacherIds.remove(teacherId);
    }

    public int getTeacherCount() {
        return teacherIds.size();
    }

    public boolean isWithinBudget(double amount) {
        return amount <= budget;
    }

    @Override
    public String toString() {
        return String.format("Department{id='%s', name='%s', head='%s', budget=%.2f, teacherCount=%d}",
                id, name, head, budget, getTeacherCount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}