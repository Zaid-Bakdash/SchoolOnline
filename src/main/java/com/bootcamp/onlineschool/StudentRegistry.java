package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Student;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StudentRegistry class demonstrating:
 * - Collections (ArrayList, HashMap)
 * - Iteration (for-each, Iterator)
 * - Streams API
 * - Lambda expressions
 * - Sorting
 */
public class StudentRegistry {
    private List<Student> students;
    private Map<String, Student> studentMap;
    
    public StudentRegistry() {
        this.students = new ArrayList<>();
        this.studentMap = new HashMap<>();
    }
    
    /**
     * Add a student to the registry
     */
    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (!student.isValidEmail()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        students.add(student);
        studentMap.put(student.getStudentId(), student);
    }
    
    /**
     * Remove a student by ID
     */
    public boolean removeStudent(String studentId) {
        Student student = studentMap.remove(studentId);
        if (student != null) {
            students.remove(student);
            return true;
        }
        return false;
    }
    
    /**
     * Find a student by ID
     */
    public Student findStudentById(String studentId) {
        return studentMap.get(studentId);
    }
    
    /**
     * Find a student by email (case-insensitive)
     */
    public Student findStudentByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return students.stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Find students by name (partial match)
     */
    public List<Student> findStudentsByName(String name) {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all students sorted by name
     */
    public List<Student> getAllStudentsSortedByName() {
        return students.stream()
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all students sorted by GPA (descending)
     */
    public List<Student> getAllStudentsSortedByGpa() {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * Get all students sorted using a custom Comparator
     */
    public List<Student> getAllStudentsSorted(Comparator<Student> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        return students.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    
    /**
     * Get students with GPA above threshold
     */
    public List<Student> getStudentsWithHighGpa(double threshold) {
        return students.stream()
                .filter(s -> s.getGpa() >= threshold)
                .collect(Collectors.toList());
    }

    /**
     * Find students with GPA within the given inclusive range [min, max]
     */
    public List<Student> findStudentsByGpaRange(double min, double max) {
        if (min > max) {
            return Collections.emptyList();
        }

        return students.stream()
                .filter(s -> s.getGpa() >= min && s.getGpa() <= max)
                .collect(Collectors.toList());
    }

    /**
     * Find students whose email ends with the specified domain (case-insensitive)
     */
    public List<Student> findStudentsByEmailDomain(String domain) {
        if (domain == null || domain.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalizedDomain = domain.trim();
        if (normalizedDomain.startsWith("@")) {
            normalizedDomain = normalizedDomain.substring(1);
        }

        String domainToMatch = normalizedDomain.toLowerCase();

        return students.stream()
                .filter(s -> {
                    String email = s.getEmail();
                    if (email == null || !email.contains("@")) {
                        return false;
                    }
                    String emailDomain = email.substring(email.indexOf("@") + 1);
                    return emailDomain.equalsIgnoreCase(domainToMatch);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Display all students
     */
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in registry");
            return;
        }
        students.forEach(System.out::println);
    }
    
    /**
     * Get total number of students
     */
    public int getStudentCount() {
        return students.size();
    }
    
    /**
     * Get average GPA
     */
    public double getAverageGpa() {
        if (students.isEmpty()) {
            return 0.0;
        }
        return students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Get GPA distribution as a map of grade letters to counts
     * A: 3.7-4.0, B: 2.7-3.69, C: 1.7-2.69, D: 1.0-1.69, F: 0.0-0.99
     */
    public Map<String, Integer> getGpaDistribution() {
        return students.stream()
                .collect(Collectors.groupingBy(
                        this::getGradeLetter,
                        Collectors.summingInt(s -> 1)
                ));
    }
    
    /**
     * Helper method to convert GPA to grade letter
     */
    private String getGradeLetter(Student student) {
        double gpa = student.getGpa();
        if (gpa >= 3.7) return "A";
        if (gpa >= 2.7) return "B";
        if (gpa >= 1.7) return "C";
        if (gpa >= 1.0) return "D";
        return "F";
    }
    
    /**
     * Get top N students by GPA (descending order)
     */
    public List<Student> getTopStudents(int n) {
        if (n <= 0) {
            return Collections.emptyList();
        }
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
    
    /**
     * Get students above the given GPA percentile (0-100)
     * For example, percentile 50 returns students above the median GPA
     */
    public List<Student> getStudentsByGpaPercentile(double percentile) {
        if (percentile < 0 || percentile > 100 || students.isEmpty()) {
            return Collections.emptyList();
        }
        
        if (percentile == 0.0) {
            return new ArrayList<>(students);
        }
        
        if (percentile == 100.0) {
            return Collections.emptyList();
        }
        
        // Get sorted GPAs
        List<Double> gpas = students.stream()
                .map(Student::getGpa)
                .sorted()
                .collect(Collectors.toList());
        
        // Calculate percentile index
        int index = (int) Math.ceil((percentile / 100.0) * gpas.size()) - 1;
        if (index < 0) index = 0;
        
        double percentileValue = gpas.get(index);
        
        // Return students with GPA >= percentile value
        return students.stream()
                .filter(s -> s.getGpa() >= percentileValue)
                .collect(Collectors.toList());
    }
    
    /**
     * Clear all students
     */
    public void clear() {
        students.clear();
        studentMap.clear();
    }
}
