package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Department;
import com.bootcamp.onlineschool.model.Teacher;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.DepartmentRepository;
import com.bootcamp.onlineschool.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DepartmentService - Service layer for Department entity
 */
@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    public DepartmentService(DepartmentRepository departmentRepository,
                             TeacherRepository teacherRepository,
                             CourseRepository courseRepository) {
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    public Department createDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        if (departmentRepository.findByCode(department.getCode()).isPresent()) {
            throw new DepartmentAlreadyExistsException("Department code already exists: " + department.getCode());
        }
        if (departmentRepository.findByName(department.getName()).isPresent()) {
            throw new DepartmentAlreadyExistsException("Department name already exists: " + department.getName());
        }
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found: " + id));
    }

    @Transactional(readOnly = true)
    public Department getDepartmentByCode(String code) {
        return departmentRepository.findByCode(code)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with code: " + code));
    }

    @Transactional(readOnly = true)
    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with name: " + name));
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Department> getDepartmentsWithBudgetGreaterThan(Double budget) {
        return departmentRepository.findByBudgetGreaterThan(budget);
    }

    @Transactional(readOnly = true)
    public Department getDepartmentWithTeachers(Long departmentId) {
        return departmentRepository.findByIdWithTeachers(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found: " + departmentId));
    }

    @Transactional(readOnly = true)
    public Department getDepartmentWithCourses(Long departmentId) {
        return departmentRepository.findByIdWithCourses(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found: " + departmentId));
    }

    public Department updateDepartment(Long id, Department details) {
        Department existing = getDepartmentById(id);
        if (details.getName() != null) {
            existing.setName(details.getName());
        }
        if (details.getCode() != null) {
            existing.setCode(details.getCode());
        }
        if (details.getBudget() != null) {
            existing.setBudget(details.getBudget());
        }
        if (details.getLocation() != null) {
            existing.setLocation(details.getLocation());
        }
        return departmentRepository.save(existing);
    }

    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new DepartmentNotFoundException("Department not found: " + id);
        }
        departmentRepository.deleteById(id);
    }

    public Department assignTeacherToDepartment(Long departmentId, Long teacherId) {
        Department department = getDepartmentById(departmentId);
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found: " + teacherId));

        if (teacher.getDepartment() != null && !teacher.getDepartment().equals(department)) {
            teacher.getDepartment().removeTeacher(teacher);
        }
        department.addTeacher(teacher);
        teacherRepository.save(teacher);
        return department;
    }

    public Department removeTeacherFromDepartment(Long departmentId, Long teacherId) {
        Department department = getDepartmentById(departmentId);
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found: " + teacherId));

        department.removeTeacher(teacher);
        teacherRepository.save(teacher);
        return department;
    }

    public Department assignCourseToDepartment(Long departmentId, Long courseId) {
        Department department = getDepartmentById(departmentId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + courseId));

        if (course.getDepartment() != null && !course.getDepartment().equals(department)) {
            course.getDepartment().removeCourse(course);
        }
        department.addCourse(course);
        courseRepository.save(course);
        return department;
    }

    public Department removeCourseFromDepartment(Long departmentId, Long courseId) {
        Department department = getDepartmentById(departmentId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + courseId));

        department.removeCourse(course);
        courseRepository.save(course);
        return department;
    }

    @Transactional(readOnly = true)
    public long countTeachersInDepartment(Long departmentId) {
        Department department = getDepartmentById(departmentId);
        return departmentRepository.countTeachersByDepartment(department);
    }

    @Transactional(readOnly = true)
    public Double getTotalBudget() {
        return departmentRepository.getTotalBudget();
    }

    public static class DepartmentNotFoundException extends RuntimeException {
        public DepartmentNotFoundException(String message) {
            super(message);
        }
    }

    public static class DepartmentAlreadyExistsException extends RuntimeException {
        public DepartmentAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class TeacherNotFoundException extends RuntimeException {
        public TeacherNotFoundException(String message) {
            super(message);
        }
    }

    public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(String message) {
            super(message);
        }
    }
}
