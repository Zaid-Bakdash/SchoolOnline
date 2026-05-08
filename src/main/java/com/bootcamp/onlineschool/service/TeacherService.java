package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Teacher;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * TeacherService - Service layer for Teacher entity
 * Handles business logic and transaction management
 */
@Service
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    /**
     * Constructor injection
     */
    public TeacherService(TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Create a new teacher
     */
    public Teacher createTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }
        if (teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
            throw new TeacherAlreadyExistsException("Teacher with email " + teacher.getEmail() + " already exists");
        }
        return teacherRepository.save(teacher);
    }

    /**
     * Get teacher by ID
     */
    @Transactional(readOnly = true)
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found: " + id));
    }

    /**
     * Get all teachers
     */
    @Transactional(readOnly = true)
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * Get teacher by email
     */
    @Transactional(readOnly = true)
    public Teacher getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with email: " + email));
    }

    /**
     * Get teachers by department
     */
    @Transactional(readOnly = true)
    public List<Teacher> getTeachersByDepartment(String department) {
        return teacherRepository.findByDepartment(department);
    }

    /**
     * Get teachers with minimum years of experience
     */
    @Transactional(readOnly = true)
    public List<Teacher> getTeachersWithExperience(Integer years) {
        return teacherRepository.findByYearsOfExperienceGreaterThanEqual(years);
    }

    /**
     * Get teachers with salary in range
     */
    @Transactional(readOnly = true)
    public List<Teacher> getTeachersWithSalaryRange(Double min, Double max) {
        return teacherRepository.findBySalaryBetween(min, max);
    }

    /**
     * Get teacher with courses eagerly loaded
     */
    @Transactional(readOnly = true)
    public Teacher getTeacherWithCourses(Long id) {
        return teacherRepository.findByIdWithCourses(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found: " + id));
    }

    /**
     * Get teachers with minimum number of courses
     */
    @Transactional(readOnly = true)
    public List<Teacher> getTeachersWithMinCourses(int minCourses) {
        return teacherRepository.findTeachersWithMinCourses(minCourses);
    }

    /**
     * Get average salary by department
     */
    @Transactional(readOnly = true)
    public Double getAverageSalaryByDepartment(String department) {
        return teacherRepository.getAverageSalaryByDepartment(department);
    }

    /**
     * Assign a course to a teacher
     */
    public void assignCourseToTeacher(Long teacherId, Long courseId) {
        Teacher teacher = getTeacherById(teacherId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseService.CourseNotFoundException("Course not found: " + courseId));
        
        teacher.addCourse(course);
        teacherRepository.save(teacher);
    }

    /**
     * Remove a course from a teacher
     */
    public void removeCourseFromTeacher(Long teacherId, Long courseId) {
        Teacher teacher = getTeacherById(teacherId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseService.CourseNotFoundException("Course not found: " + courseId));
        
        teacher.removeCourse(course);
        teacherRepository.save(teacher);
    }

    /**
     * Update teacher information
     */
    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        Teacher teacher = getTeacherById(id);
        
        if (teacherDetails.getName() != null) {
            teacher.setName(teacherDetails.getName());
        }
        if (teacherDetails.getEmail() != null && !teacherDetails.getEmail().equals(teacher.getEmail())) {
            if (teacherRepository.findByEmail(teacherDetails.getEmail()).isPresent()) {
                throw new TeacherAlreadyExistsException("Teacher with email " + teacherDetails.getEmail() + " already exists");
            }
            teacher.setEmail(teacherDetails.getEmail());
        }
        if (teacherDetails.getDepartment() != null) {
            teacher.setDepartment(teacherDetails.getDepartment());
        }
        if (teacherDetails.getYearsOfExperience() != null) {
            teacher.setYearsOfExperience(teacherDetails.getYearsOfExperience());
        }
        if (teacherDetails.getSalary() != null) {
            teacher.setSalary(teacherDetails.getSalary());
        }
        
        return teacherRepository.save(teacher);
    }

    /**
     * Delete teacher
     */
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new TeacherNotFoundException("Teacher not found: " + id);
        }
        teacherRepository.deleteById(id);
    }

    /**
     * Get total number of teachers
     */
    @Transactional(readOnly = true)
    public Long getTotalTeachers() {
        return teacherRepository.count();
    }

    // Custom Exceptions
    public static class TeacherNotFoundException extends RuntimeException {
        public TeacherNotFoundException(String message) {
            super(message);
        }
    }

    public static class TeacherAlreadyExistsException extends RuntimeException {
        public TeacherAlreadyExistsException(String message) {
            super(message);
        }
    }
}
