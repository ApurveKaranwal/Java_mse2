package com.example.studentmanagement;

import com.example.studentmanagement.dto.MeritStudentDTO;
import com.example.studentmanagement.dto.StudentRequestDTO;
import com.example.studentmanagement.dto.StudentResponseDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.DuplicateStudentIdException;
import com.example.studentmanagement.exception.InvalidScoreException;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"h2", "test"})
@Transactional
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    @DisplayName("Req 1: Calculate each student's average score")
    void testCalculateAverageScore() {
        Map<String, Double> scores = new HashMap<>();
        scores.put("Mathematics", 90.0);
        scores.put("Programming", 80.0);
        scores.put("Science", 85.0);

        double average = studentService.calculateAverageScore(scores);
        assertEquals(85.0, average, 0.001);
    }

    @Test
    @DisplayName("Req 2: Assign performance category based on average")
    void testAssignPerformanceCategory() {
        assertEquals("Excellent", studentService.assignPerformanceCategory(85.0));
        assertEquals("Excellent", studentService.assignPerformanceCategory(94.5));

        assertEquals("Good", studentService.assignPerformanceCategory(84.99));
        assertEquals("Good", studentService.assignPerformanceCategory(70.0));

        assertEquals("Satisfactory", studentService.assignPerformanceCategory(69.99));
        assertEquals("Satisfactory", studentService.assignPerformanceCategory(50.0));

        assertEquals("Needs Improvement", studentService.assignPerformanceCategory(49.99));
        assertEquals("Needs Improvement", studentService.assignPerformanceCategory(32.0));
    }

    @Test
    @DisplayName("Req 3: Identify the stronger subject for each student")
    void testIdentifyStrongerSubject() {
        Map<String, Double> scores = new HashMap<>();
        scores.put("Mathematics", 75.0);
        scores.put("Programming", 95.0);
        scores.put("Science", 82.0);

        String stronger = studentService.identifyStrongerSubject(scores);
        assertTrue(stronger.startsWith("Programming"));
        assertTrue(stronger.contains("95.0"));
    }

    @Test
    @DisplayName("Req 4: Search for students by ID, email or course")
    void testSearchStudents() {
        StudentRequestDTO s1 = new StudentRequestDTO(
                "STU201", "Alice Wonder", "alice@example.com", "Computer Science",
                Set.of("Java"), Map.of("Mathematics", 90.0, "Programming", 85.0)
        );
        studentService.createStudent(s1);

        // Search by ID
        StudentResponseDTO byId = studentService.searchByStudentId("STU201");
        assertNotNull(byId);
        assertEquals("Alice Wonder", byId.getName());

        // Search by Email
        StudentResponseDTO byEmail = studentService.searchByEmail("alice@example.com");
        assertNotNull(byEmail);
        assertEquals("STU201", byEmail.getStudentId());

        // Search by Course
        List<StudentResponseDTO> byCourse = studentService.searchByCourse("Computer Science");
        assertEquals(1, byCourse.size());
    }

    @Test
    @DisplayName("Req 5: Generate merit list sorted by average score descending")
    void testGenerateMeritList() {
        StudentRequestDTO sLow = new StudentRequestDTO(
                "STU301", "Low Scorer", "low@example.com", "CS",
                Set.of("Java"), Map.of("Math", 50.0, "Programming", 50.0) // Avg 50.0
        );
        StudentRequestDTO sHigh = new StudentRequestDTO(
                "STU302", "High Scorer", "high@example.com", "CS",
                Set.of("Python"), Map.of("Math", 95.0, "Programming", 95.0) // Avg 95.0
        );
        studentService.createStudent(sLow);
        studentService.createStudent(sHigh);

        List<MeritStudentDTO> meritList = studentService.generateMeritList();
        assertEquals(2, meritList.size());
        assertEquals("STU302", meritList.get(0).getStudentId()); // Rank 1
        assertEquals(1, meritList.get(0).getRank());
        assertEquals("STU301", meritList.get(1).getStudentId()); // Rank 2
        assertEquals(2, meritList.get(1).getRank());
    }

    @Test
    @DisplayName("Req 6: Identify students who have the same technical skills")
    void testGroupStudentsBySkill() {
        studentService.createStudent(new StudentRequestDTO(
                "STU401", "Student A", "a@example.com", "CS",
                Set.of("Java", "SQL"), Map.of("Math", 80.0)
        ));
        studentService.createStudent(new StudentRequestDTO(
                "STU402", "Student B", "b@example.com", "IT",
                Set.of("Java", "Python"), Map.of("Math", 85.0)
        ));

        Map<String, List<StudentResponseDTO>> grouped = studentService.groupStudentsBySkill();
        assertTrue(grouped.containsKey("Java"));
        assertEquals(2, grouped.get("Java").size()); // Both have Java
        assertEquals(1, grouped.get("SQL").size());
        assertEquals(1, grouped.get("Python").size());
    }

    @Test
    @DisplayName("Req 7: Display students needing improvement in programming (< 50) with Math >= 75")
    void testGetStudentsNeedingProgrammingImprovementWithHighMath() {
        // Matches Req 7: Programming = 40 (< 50) and Math = 82 (>= 75)
        studentService.createStudent(new StudentRequestDTO(
                "STU501", "Math Genius But Needs Coding Help", "mathgenius@example.com", "CS",
                Set.of("Math"), Map.of("Mathematics", 82.0, "Programming", 40.0)
        ));

        // Does not match: Programming is high (80 >= 50)
        studentService.createStudent(new StudentRequestDTO(
                "STU502", "All Rounder", "allrounder@example.com", "CS",
                Set.of("Java"), Map.of("Mathematics", 85.0, "Programming", 80.0)
        ));

        // Does not match: Math is low (65 < 75) even though programming is 40
        studentService.createStudent(new StudentRequestDTO(
                "STU503", "Low Math", "lowmath@example.com", "CS",
                Set.of("SQL"), Map.of("Mathematics", 65.0, "Programming", 40.0)
        ));

        List<StudentResponseDTO> results = studentService.getStudentsNeedingProgrammingImprovementWithHighMath();
        assertEquals(1, results.size());
        assertEquals("STU501", results.get(0).getStudentId());
    }

    @Test
    @DisplayName("Req 8: Prevent duplicate student IDs")
    void testPreventDuplicateStudentId() {
        StudentRequestDTO s1 = new StudentRequestDTO(
                "STU601", "Student One", "s1@example.com", "CS",
                Set.of("Java"), Map.of("Math", 80.0)
        );
        studentService.createStudent(s1);

        // Attempting to create duplicate student ID STU601
        StudentRequestDTO duplicate = new StudentRequestDTO(
                "STU601", "Student Two", "s2@example.com", "IT",
                Set.of("Python"), Map.of("Math", 90.0)
        );

        assertThrows(DuplicateStudentIdException.class, () -> {
            studentService.createStudent(duplicate);
        });
    }

    @Test
    @DisplayName("Req 8: Validate scores between 0 and 100")
    void testValidateScoresBetween0And100() {
        // Score > 100
        StudentRequestDTO invalidHigh = new StudentRequestDTO(
                "STU701", "Invalid High", "high@example.com", "CS",
                Set.of("Java"), Map.of("Math", 105.0)
        );
        assertThrows(InvalidScoreException.class, () -> {
            studentService.createStudent(invalidHigh);
        });

        // Score < 0
        StudentRequestDTO invalidLow = new StudentRequestDTO(
                "STU702", "Invalid Low", "low@example.com", "CS",
                Set.of("Java"), Map.of("Math", -10.0)
        );
        assertThrows(InvalidScoreException.class, () -> {
            studentService.createStudent(invalidLow);
        });
    }
}
