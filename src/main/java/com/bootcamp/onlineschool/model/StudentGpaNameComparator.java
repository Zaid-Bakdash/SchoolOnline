package com.bootcamp.onlineschool.model;

import java.util.Comparator;

/**
 * Comparator for sorting students by GPA (descending), then by name (ascending)
 * Primary: GPA descending (highest first)
 * Secondary: Name ascending (case-insensitive) for tie-breaking
 * Demonstrates: Chained comparators, multi-field comparison
 */
public class StudentGpaNameComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        if (s1 == null && s2 == null) {
            return 0;
        }
        if (s1 == null) {
            return 1;
        }
        if (s2 == null) {
            return -1;
        }

        // First compare by GPA (descending - reverse order)
        int gpaComparison = Double.compare(s2.getGpa(), s1.getGpa());
        if (gpaComparison != 0) {
            return gpaComparison;
        }

        // If GPA is the same, compare by name (ascending, case-insensitive)
        return s1.getName().compareToIgnoreCase(s2.getName());
    }
}
