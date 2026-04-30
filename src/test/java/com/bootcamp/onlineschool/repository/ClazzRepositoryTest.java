package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Clazz;
import com.bootcamp.onlineschool.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ClazzRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClazzRepository clazzRepository;

    private Teacher testTeacher1;
    private Teacher testTeacher2;
    private Clazz testClazz1;
    private Clazz testClazz2;
    private Clazz testClazz3;

    @BeforeEach
    void setUp() {
        testTeacher1 = new Teacher("EMP001", "Prof. Wilson", "prof.wilson@example.com", "Computer Science", 5, 75000.0, LocalDate.of(2020, 8, 1));
        testTeacher2 = new Teacher("EMP002", "Dr. Johnson", "dr.johnson@example.com", "Mathematics", 10, 85000.0, LocalDate.of(2019, 9, 15));

        entityManager.persistAndFlush(testTeacher1);
        entityManager.persistAndFlush(testTeacher2);

        testClazz1 = new Clazz("Java Programming", "Fall", 2023, 30, testTeacher1);
        testClazz2 = new Clazz("Data Structures", "Spring", 2024, 25, testTeacher1);
        testClazz3 = new Clazz("Calculus I", "Fall", 2023, 40, testTeacher2);

        entityManager.persistAndFlush(testClazz1);
        entityManager.persistAndFlush(testClazz2);
        entityManager.persistAndFlush(testClazz3);
    }

    @Test
    void findByName_ShouldReturnClazz_WhenNameExists() {
        // When
        Optional<Clazz> foundClazz = clazzRepository.findByName("Java Programming");

        // Then
        assertThat(foundClazz).isPresent();
        assertThat(foundClazz.get().getName()).isEqualTo("Java Programming");
        assertThat(foundClazz.get().getSemester()).isEqualTo("Fall");
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenNameDoesNotExist() {
        // When
        Optional<Clazz> foundClazz = clazzRepository.findByName("Nonexistent Class");

        // Then
        assertThat(foundClazz).isEmpty();
    }

    @Test
    void findBySemester_ShouldReturnClassesInSemester() {
        // When
        List<Clazz> fallClasses = clazzRepository.findBySemester("Fall");

        // Then
        assertThat(fallClasses).hasSize(2);
        assertThat(fallClasses).extracting(Clazz::getName)
                .containsExactlyInAnyOrder("Java Programming", "Calculus I");
    }

    @Test
    void findByYear_ShouldReturnClassesInYear() {
        // When
        List<Clazz> classes2023 = clazzRepository.findByYear(2023);

        // Then
        assertThat(classes2023).hasSize(2);
        assertThat(classes2023).extracting(Clazz::getName)
                .containsExactlyInAnyOrder("Java Programming", "Calculus I");
    }

    @Test
    void findByTeacher_ShouldReturnClassesTaughtByTeacher() {
        // When
        List<Clazz> wilsonClasses = clazzRepository.findByTeacher(testTeacher1);

        // Then
        assertThat(wilsonClasses).hasSize(2);
        assertThat(wilsonClasses).extracting(Clazz::getName)
                .containsExactlyInAnyOrder("Java Programming", "Data Structures");
    }

    @Test
    void findByTeacherId_ShouldReturnClassesTaughtByTeacherId() {
        // When
        List<Clazz> johnsonClasses = clazzRepository.findByTeacherId(testTeacher2.getId());

        // Then
        assertThat(johnsonClasses).hasSize(1);
        assertThat(johnsonClasses.get(0).getName()).isEqualTo("Calculus I");
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenNameExists() {
        // When
        boolean exists = clazzRepository.existsByName("Data Structures");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenNameDoesNotExist() {
        // When
        boolean exists = clazzRepository.existsByName("Nonexistent Class");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void findClassesBySemesterAndYear_ShouldReturnClassesMatchingBothCriteria() {
        // When
        List<Clazz> fallClasses2023 = clazzRepository.findClassesBySemesterAndYear("Fall", 2023);

        // Then
        assertThat(fallClasses2023).hasSize(2);
        assertThat(fallClasses2023).extracting(Clazz::getName)
                .containsExactlyInAnyOrder("Java Programming", "Calculus I");
    }

    @Test
    void findBySemesterAndYear_ShouldReturnClassesMatchingBothCriteria() {
        // When
        List<Clazz> springClasses2024 = clazzRepository.findBySemesterAndYear("Spring", 2024);

        // Then
        assertThat(springClasses2024).hasSize(1);
        assertThat(springClasses2024.get(0).getName()).isEqualTo("Data Structures");
    }

    @Test
    void findClassesBySemesterAndYear_ShouldReturnEmptyList_WhenNoCriteriaMatch() {
        // When
        List<Clazz> summerClasses2025 =
                clazzRepository.findClassesBySemesterAndYear("Summer", 2025);

        // Then
        assertThat(summerClasses2025).isEmpty();
    }

    @Test
    void findByMaxCapacityGreaterThanEqual_ShouldReturnClassesWithMinimumCapacity() {
        // When
        List<Clazz> largeClasses = clazzRepository.findByMaxCapacityGreaterThanEqual(30);

        // Then
        assertThat(largeClasses).hasSize(2);
        assertThat(largeClasses).extracting(Clazz::getName)
                .containsExactlyInAnyOrder("Java Programming", "Calculus I");
    }
}
