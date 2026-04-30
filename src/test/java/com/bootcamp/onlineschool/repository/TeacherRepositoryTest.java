package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher testTeacher1;
    private Teacher testTeacher2;
    private Teacher testTeacher3;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
        entityManager.clear();

        testTeacher1 = new Teacher("T001", "Prof. Wilson", "prof.wilson@example.com", "Computer Science", 5, 75000.0, LocalDate.of(2020, 8, 1));
        testTeacher2 = new Teacher("T002", "Dr. Johnson", "dr.johnson@example.com", "Mathematics", 10, 85000.0, LocalDate.of(2019, 9, 15));
        testTeacher3 = new Teacher("T003", "Prof. Davis", "prof.davis@example.com", "Computer Science", 3, 65000.0, LocalDate.of(2021, 1, 10));

        entityManager.persistAndFlush(testTeacher1);
        entityManager.persistAndFlush(testTeacher2);
        entityManager.persistAndFlush(testTeacher3);
    }

    @Test
    void save_ShouldPersistTeacher() {
        // Given
        Teacher newTeacher = new Teacher("T004", "Dr. Smith", "dr.smith@example.com", "Physics", 8, 78000.0, LocalDate.of(2018, 5, 20));

        // When
        Teacher savedTeacher = teacherRepository.save(newTeacher);

        // Then
        assertThat(savedTeacher.getId()).isNotNull();
        assertThat(savedTeacher.getTeacherId()).isEqualTo("T004");
        assertThat(savedTeacher.getName()).isEqualTo("Dr. Smith");
    }

    @Test
    void findById_ShouldReturnTeacher() {
        // When
        Teacher foundTeacher = teacherRepository.findById(testTeacher1.getId()).orElse(null);

        // Then
        assertThat(foundTeacher).isNotNull();
        assertThat(foundTeacher.getName()).isEqualTo("Prof. Wilson");
    }

    @Test
    void findByTeacherId_ShouldReturnTeacher() {
        // When
        Teacher foundTeacher = teacherRepository.findByTeacherId("T001");

        // Then
        assertThat(foundTeacher).isNotNull();
        assertThat(foundTeacher.getName()).isEqualTo("Prof. Wilson");
    }

    @Test
    void findByEmail_ShouldReturnTeacher() {
        // When
        Teacher foundTeacher = teacherRepository.findByEmail("dr.johnson@example.com");

        // Then
        assertThat(foundTeacher).isNotNull();
        assertThat(foundTeacher.getDepartment()).isEqualTo("Mathematics");
    }

    @Test
    void findByDepartment_ShouldReturnTeachersInDepartment() {
        // When
        List<Teacher> csTeachers = teacherRepository.findByDepartment("Computer Science");

        // Then
        assertThat(csTeachers).hasSize(2);
        assertThat(csTeachers).extracting(Teacher::getName)
                .containsExactlyInAnyOrder("Prof. Wilson", "Prof. Davis");
    }

    @Test
    void findByYearsOfExperienceGreaterThanEqual_ShouldReturnExperiencedTeachers() {
        // When
        List<Teacher> experiencedTeachers = teacherRepository.findByYearsOfExperienceGreaterThanEqual(5);

        // Then
        assertThat(experiencedTeachers).hasSize(2);
        assertThat(experiencedTeachers).extracting(Teacher::getName)
                .containsExactlyInAnyOrder("Prof. Wilson", "Dr. Johnson");
    }

    @Test
    void findBySalaryBetween_ShouldReturnTeachersInSalaryRange() {
        // When
        List<Teacher> midSalaryTeachers = teacherRepository.findBySalaryBetween(70000.0, 80000.0);

        // Then
        assertThat(midSalaryTeachers).hasSize(1);
        assertThat(midSalaryTeachers.get(0).getName()).isEqualTo("Prof. Wilson");
    }

    @Test
    void findAllTeachersSortedByName_ShouldReturnSortedList() {
        // When
        List<Teacher> sortedTeachers = teacherRepository.findAllTeachersSortedByName();

        // Then
        assertThat(sortedTeachers).hasSize(3);
        assertThat(sortedTeachers.get(0).getName()).isEqualTo("Dr. Johnson");
        assertThat(sortedTeachers.get(1).getName()).isEqualTo("Prof. Davis");
        assertThat(sortedTeachers.get(2).getName()).isEqualTo("Prof. Wilson");
    }

    @Test
    void findTeachersHiredAfter_ShouldReturnRecentlyHiredTeachers() {
        // When
        List<Teacher> recentTeachers = teacherRepository.findTeachersHiredAfter(LocalDate.of(2020, 1, 1));

        // Then
        assertThat(recentTeachers).hasSize(2);
        assertThat(recentTeachers).extracting(Teacher::getName)
                .containsExactlyInAnyOrder("Prof. Wilson", "Prof. Davis");
    }

    @Test
    void countTeachersByDepartment_ShouldReturnCorrectCount() {
        // When
        Long csCount = teacherRepository.countTeachersByDepartment("Computer Science");

        // Then
        assertThat(csCount).isEqualTo(2);
    }

    @Test
    void calculateAverageSalaryByDepartment_ShouldReturnCorrectAverage() {
        // When
        Double avgSalary = teacherRepository.calculateAverageSalaryByDepartment("Computer Science");

        // Then
        assertThat(avgSalary).isEqualTo(70000.0);
    }

    @Test
    void findTopNHighestPaidTeachers_ShouldReturnSortedBySalaryDesc() {
        // When
        List<Teacher> topTeachers = teacherRepository.findTopNHighestPaidTeachers();

        // Then
        assertThat(topTeachers).hasSize(3);
        assertThat(topTeachers.get(0).getName()).isEqualTo("Dr. Johnson");
        assertThat(topTeachers.get(1).getName()).isEqualTo("Prof. Wilson");
        assertThat(topTeachers.get(2).getName()).isEqualTo("Prof. Davis");
    }

    @Test
    void delete_ShouldRemoveTeacher() {
        // Given
        Long idToDelete = testTeacher1.getId();

        // When
        teacherRepository.deleteById(idToDelete);

        // Then
        assertThat(teacherRepository.findById(idToDelete)).isEmpty();
    }
}