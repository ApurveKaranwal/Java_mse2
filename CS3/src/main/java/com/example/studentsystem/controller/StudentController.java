package com.example.studentsystem.controller;

import com.example.studentsystem.dto.ApiResponse;
import com.example.studentsystem.dto.MeritStudentDTO;
import com.example.studentsystem.dto.ScoreSkillsUpdateDTO;
import com.example.studentsystem.dto.StudentRequestDTO;
import com.example.studentsystem.entity.Student;
import com.example.studentsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Student Profile and Skill Development Management System.
 *
 * Implements the 6 required REST APIs:
 * 1. POST   /api/students                   -> Add a student
 * 2. GET    /api/students                   -> Retrieve all students
 * 3. GET    /api/students/{id}              -> Search for a student by ID
 * 4. PUT    /api/students/{id}/scores-and-skills -> Update student scores and skills
 * 5. DELETE /api/students/{id}              -> Delete a student record
 * 6. GET    /api/students/merit-list        -> Display the merit list
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // =========================================================================
    // 1. ADD A STUDENT
    // POST http://localhost:8080/api/students
    // =========================================================================
    @PostMapping
    public ResponseEntity<ApiResponse<Student>> addStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        Student savedStudent = studentService.addStudent(requestDTO);
        return new ResponseEntity<>(
                ApiResponse.success("Student added successfully!", savedStudent),
                HttpStatus.CREATED
        );
    }

    // =========================================================================
    // 2. RETRIEVE ALL STUDENTS
    // GET http://localhost:8080/api/students
    // =========================================================================
    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success("Retrieved all students successfully!", students));
    }

    // =========================================================================
    // 6. DISPLAY THE MERIT LIST (Defined before /{id} to ensure clean path routing)
    // GET http://localhost:8080/api/students/merit-list
    // =========================================================================
    @GetMapping("/merit-list")
    public ResponseEntity<ApiResponse<List<MeritStudentDTO>>> getMeritList() {
        List<MeritStudentDTO> meritList = studentService.getMeritList();
        return ResponseEntity.ok(ApiResponse.success("Merit list generated successfully!", meritList));
    }

    // =========================================================================
    // 3. SEARCH FOR A STUDENT BY ID
    // GET http://localhost:8080/api/students/{id}
    // =========================================================================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success("Student found with ID: " + id, student));
    }

    // =========================================================================
    // 4. UPDATE STUDENT SCORES AND SKILLS
    // PUT http://localhost:8080/api/students/{id}/scores-and-skills
    // =========================================================================
    @PutMapping("/{id}/scores-and-skills")
    public ResponseEntity<ApiResponse<Student>> updateScoresAndSkills(
            @PathVariable Long id,
            @Valid @RequestBody ScoreSkillsUpdateDTO updateDTO) {
        Student updatedStudent = studentService.updateScoresAndSkills(id, updateDTO);
        return ResponseEntity.ok(
                ApiResponse.success("Student scores and skills updated successfully!", updatedStudent)
        );
    }

    // Also support general PUT on /api/students/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> updateStudentFull(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        Student updatedStudent = studentService.updateStudentFull(id, requestDTO);
        return ResponseEntity.ok(
                ApiResponse.success("Student details updated successfully!", updatedStudent)
        );
    }

    // =========================================================================
    // 5. DELETE A STUDENT RECORD
    // DELETE http://localhost:8080/api/students/{id}
    // =========================================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(
                ApiResponse.success("Student record with ID " + id + " deleted successfully!", null)
        );
    }
}
