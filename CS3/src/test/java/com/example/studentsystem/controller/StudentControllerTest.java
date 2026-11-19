package com.example.studentsystem.controller;

import com.example.studentsystem.dto.MeritStudentDTO;
import com.example.studentsystem.dto.ScoreSkillsUpdateDTO;
import com.example.studentsystem.dto.StudentRequestDTO;
import com.example.studentsystem.entity.Student;
import com.example.studentsystem.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Endpoint 1: POST /api/students -> Add a student")
    void testAddStudentEndpoint() throws Exception {
        StudentRequestDTO request = new StudentRequestDTO("John Doe", "john@example.com", "CSE", 3, 88.0,
                Arrays.asList("Java", "Spring Boot"));

        Student student = new Student("John Doe", "john@example.com", "CSE", 3, 88.0,
                Arrays.asList("Java", "Spring Boot"));
        student.setId(1L);
        student.setGrade("A (Excellent)");

        when(studentService.addStudent(any(StudentRequestDTO.class))).thenReturn(student);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.grade").value("A (Excellent)"));
    }

    @Test
    @DisplayName("Endpoint 2: GET /api/students -> Retrieve all students")
    void testGetAllStudentsEndpoint() throws Exception {
        Student s1 = new Student("Alice", "alice@test.com", "IT", 2, 90.0, List.of("Java"));
        s1.setId(1L);

        when(studentService.getAllStudents()).thenReturn(List.of(s1));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Alice"));
    }

    @Test
    @DisplayName("Endpoint 3: GET /api/students/{id} -> Search for a student by ID")
    void testGetStudentByIdEndpoint() throws Exception {
        Student s1 = new Student("Bob", "bob@test.com", "ECE", 4, 82.0, List.of("C++"));
        s1.setId(2L);

        when(studentService.getStudentById(2L)).thenReturn(s1);

        mockMvc.perform(get("/api/students/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Bob"));
    }

    @Test
    @DisplayName("Endpoint 4: PUT /api/students/{id}/scores-and-skills -> Update scores and skills")
    void testUpdateScoresAndSkillsEndpoint() throws Exception {
        ScoreSkillsUpdateDTO updateDTO = new ScoreSkillsUpdateDTO(94.0, Arrays.asList("Java", "Cloud"));

        Student updated = new Student("Bob", "bob@test.com", "ECE", 4, 94.0, Arrays.asList("Java", "Cloud"));
        updated.setId(2L);
        updated.setGrade("A+ (Outstanding)");

        when(studentService.updateScoresAndSkills(eq(2L), any(ScoreSkillsUpdateDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/students/2/scores-and-skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.academicScore").value(94.0))
                .andExpect(jsonPath("$.data.grade").value("A+ (Outstanding)"));
    }

    @Test
    @DisplayName("Endpoint 5: DELETE /api/students/{id} -> Delete a student record")
    void testDeleteStudentEndpoint() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Student record with ID 1 deleted successfully!"));
    }

    @Test
    @DisplayName("Endpoint 6: GET /api/students/merit-list -> Display the merit list")
    void testGetMeritListEndpoint() throws Exception {
        MeritStudentDTO m1 = new MeritStudentDTO(1, 1L, "Alice", "alice@test.com", "IT", 95.0, "A+ (Outstanding)",
                List.of("Java"), "Topper - First Rank with Distinction");
        MeritStudentDTO m2 = new MeritStudentDTO(2, 2L, "Bob", "bob@test.com", "ECE", 85.0, "A (Excellent)",
                List.of("C++"), "Distinction");

        when(studentService.getMeritList()).thenReturn(List.of(m1, m2));

        mockMvc.perform(get("/api/students/merit-list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].rank").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Alice"))
                .andExpect(jsonPath("$.data[1].rank").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Bob"));
    }
}
