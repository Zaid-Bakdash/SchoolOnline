package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Teacher;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TeacherRegistry class demonstrating:
 * - Collections (ArrayList, HashMap)
 * - Iteration (for-each, Iterator)
 * - Streams API
 * - Lambda expressions
 * - Sorting
 */
public class TeacherRegistry {
    private List<Teacher> teachers;
    private Map<String, Teacher> teacherMap;

    public TeacherRegistry() {
        this.teachers = new ArrayList<>();
        this.teacherMap = new HashMap<>();
    }

    /**
     * Add a teacher to the registry
     */
    public void addTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }
        if (!teacher.isValidEmail()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        teachers.add(teacher);
        teacherMap.put(teacher.getTeacherId(), teacher);
    }

    /**
     * Remove a teacher by ID
     */
    public boolean removeTeacher(String teacherId) {
        Teacher teacher = teacherMap.remove(teacherId);
        if (teacher != null) {
            teachers.remove(teacher);
            return true;
        }
        return false;
    }

    /**
     * Find a teacher by ID
     */
    public Teacher findTeacherById(String teacherId) {
        return teacherMap.get(teacherId);
    }

    /**
     * Find teachers by name (partial match)
     */
    public List<Teacher> findTeachersByName(String name) {
        return teachers.stream()
                .filter(t -> t.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get teachers by department
     */
    public List<Teacher> getTeachersByDepartment(String department) {
        return teachers.stream()
                .filter(t -> t.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    /**
     * Get all teachers sorted by name
     */
    public List<Teacher> getAllTeachersSortedByName() {
        return teachers.stream()
                .sorted(Comparator.comparing(Teacher::getName))
                .collect(Collectors.toList());
    }

    /**
     * Get all teachers sorted by department
     */
    public List<Teacher> getAllTeachersSortedByDepartment() {
        return teachers.stream()
                .sorted(Comparator.comparing(Teacher::getDepartment))
                .collect(Collectors.toList());
    }

    /**
     * Update teacher information
     */
    public boolean updateTeacher(String teacherId, Teacher updatedTeacher) {
        Teacher existingTeacher = teacherMap.get(teacherId);
        if (existingTeacher != null) {
            teachers.remove(existingTeacher);
            teachers.add(updatedTeacher);
            teacherMap.put(teacherId, updatedTeacher);
            return true;
        }
        return false;
    }

    /**
     * Display all teachers
     */
    public void displayAllTeachers() {
        if (teachers.isEmpty()) {
            System.out.println("No teachers in registry");
            return;
        }
        teachers.forEach(System.out::println);
    }

    /**
     * Get total number of teachers
     */
    public int getTeacherCount() {
        return teachers.size();
    }

    /**
     * Clear all teachers
     */
    public void clear() {
        teachers.clear();
        teacherMap.clear();
    }
}