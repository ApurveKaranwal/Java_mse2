package com.example.studentsystem.service;

import com.example.studentsystem.dto.MeritStudentDTO;
import com.example.studentsystem.dto.ScoreSkillsUpdateDTO;
import com.example.studentsystem.dto.StudentRequestDTO;
import com.example.studentsystem.entity.Student;
import com.example.studentsystem.exception.BadRequestException;
import com.example.studentsystem.exception.ResourceNotFoundException;
import com.example.studentsystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        student1 = new Student("Alice", "alice@example.com", "Computer Science", 4, 92.0,
                Arrays.asList("Java", "Spring Boot", "MySQL"));
        student1.setId(1L);
        student1.setGrade("A+ (Outstanding)");

        student2 = new Student("Bob", "bob@example.com", "Information Technology", 4, 78.5,
                Arrays.asList("Python", "Django"));
        student2.setId(2L);
        student2.setGrade("B (Good)");
    }

    @Test
    @DisplayName("Test 1: Add a student with business rules & skills deduplication")
    void testAddStudent() {
        StudentRequestDTO request = new StudentRequestDTO("Charlie", "charlie@example.com", "CSE", 3, 85.0,
                Arrays.asList(" Java ", "java", "Spring", "Java", ""));

        when(studentRepository.existsByEmail("charlie@example.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student s = invocation.getArgument(0);
            s.setId(3L);
            return s;
        });

        Student saved = studentService.addStudent(request);

        assertNotNull(saved);
        assertEquals("A (Excellent)", saved.getGrade());
        // Deduplicated & trimmed skills
        assertTrue(saved.getSkills().contains("Java"));
        assertTrue(saved.getSkills().contains("Spring"));
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Test 2: Retrieve all students")
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<Student> students = studentService.getAllStudents();

        assertEquals(2, students.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test 3: Search for a student by ID")
    void testGetStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        Student found = studentService.getStudentById(1L);

        assertNotNull(found);
        assertEquals("Alice", found.getName());
        assertEquals("alice@example.com", found.getEmail());
    }

    @Test
    @DisplayName("Test 3b: Search for a student by ID - Not Found")
    void testGetStudentByIdNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(99L));
    }

    @Test
    @DisplayName("Test 4: Update student scores and skills")
    void testUpdateScoresAndSkills() {
        ScoreSkillsUpdateDTO updateDTO = new ScoreSkillsUpdateDTO(95.5, Arrays.asList("Java", "Kubernetes", "AWS"));

        when(studentRepository.findById(2L)).thenReturn(Optional.of(student2));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student updated = studentService.updateScoresAndSkills(2L, updateDTO);

        assertEquals(95.5, updated.getAcademicScore());
        assertEquals("A+ (Outstanding)", updated.getGrade());
        assertTrue(updated.getSkills().contains("Kubernetes"));
    }

    @Test
    @DisplayName("Test 5: Delete a student record")
    void testDeleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        doNothing().when(studentRepository).delete(student1);

        assertDoesNotThrow(() -> studentService.deleteStudent(1L));
        verify(studentRepository, times(1)).delete(student1);
    }

    @Test
    @DisplayName("Test 6: Display the merit list sorted by academic score descending")
    void testGetMeritList() {
        Student student3 = new Student("Charlie", "charlie@test.com", "ECE", 2, 98.0, List.of("Embedded C"));
        student3.setId(3L);
        student3.setGrade("A+ (Outstanding)");

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student2, student1, student3));

        List<MeritStudentDTO> meritList = studentService.getMeritList();

        assertEquals(3, meritList.size());
        // Rank 1: Charlie (98.0)
        assertEquals(1, meritList.get(0).getRank());
        assertEquals("Charlie", meritList.get(0).getName());
        assertEquals(98.0, meritList.get(0).getAcademicScore());

        // Rank 2: Alice (92.0)
        assertEquals(2, meritList.get(1).getRank());
        assertEquals("Alice", meritList.get(1).getName());

        // Rank 3: Bob (78.5)
        assertEquals(3, meritList.get(2).getRank());
        assertEquals("Bob", meritList.get(2).getName());
    }

    @Test
    @DisplayName("Business Rule: Invalid academic score throws BadRequestException")
    void testInvalidScoreThrowsException() {
        StudentRequestDTO request = new StudentRequestDTO("Invalid", "invalid@example.com", "CSE", 1, 105.0, List.of());
        when(studentRepository.existsByEmail("invalid@example.com")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> studentService.addStudent(request));
    }
}
