package com.bootcamp.onlineschool.model;

import java.util.Comparator;

/**
 * Comparator for sorting students by email domain, then by name
 * Primary: Email domain (case-insensitive) - the part after @
 * Secondary: Name (ascending, case-insensitive) for tie-breaking
 * Demonstrates: String manipulation in comparators, domain extraction
 */
public class StudentEmailDomainComparator implements Comparator<Student> {
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

        // Extract email domains
        String domain1 = extractDomain(s1.getEmail());
        String domain2 = extractDomain(s2.getEmail());

        // First compare by email domain (case-insensitive)
        int domainComparison = domain1.compareToIgnoreCase(domain2);
        if (domainComparison != 0) {
            return domainComparison;
        }

        // If domain is the same, compare by name (ascending, case-insensitive)
        return s1.getName().compareToIgnoreCase(s2.getName());
    }

    /**
     * Extract domain from email address (the part after @)
     * Returns empty string if email is null or has no @
     */
    private String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            return "";
        }
        return email.substring(email.indexOf("@") + 1);
    }
}
