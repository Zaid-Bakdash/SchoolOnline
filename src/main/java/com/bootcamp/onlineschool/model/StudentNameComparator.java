package com.bootcamp.onlineschool.model;

import java.util.Comparator;

/**
 * Comparator for sorting students by name (case-insensitive)
 * Demonstrates: Comparator interface, case-insensitive string comparison
 */
public class StudentNameComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        if (s1 == null && s2 == null) {
            return 0;
        }
        if (s1 == null) {
            return 1;  // null comes after non-null
        }
        if (s2 == null) {
            return -1;  // non-null comes before null
        }
        return s1.getName().compareToIgnoreCase(s2.getName());
    }
}
