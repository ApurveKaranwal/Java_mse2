package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.MeritStudentDTO;
import com.example.studentmanagement.dto.StudentRequestDTO;
import com.example.studentmanagement.dto.StudentResponseDTO;
import com.example.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    // CRUD Endpoints (Requirement 8 validated on Create/Update)
    // =========================================================================

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO created = studentService.createStudent(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO updated = studentService.updateStudent(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(Map.of("message", "Student successfully deleted with ID: " + id));
    }

    // =========================================================================
    // Requirements 1, 2, 3: All students with average, category, stronger subject
    // =========================================================================

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // =========================================================================
    // Requirement 4: Search for students by ID, email or course
    // =========================================================================

    @GetMapping("/student-id/{studentId}")
    public ResponseEntity<StudentResponseDTO> searchByStudentId(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.searchByStudentId(studentId));
    }

    @GetMapping("/search/email")
    public ResponseEntity<StudentResponseDTO> searchByEmail(@RequestParam String email) {
        return ResponseEntity.ok(studentService.searchByEmail(email));
    }

    @GetMapping("/search/course")
    public ResponseEntity<List<StudentResponseDTO>> searchByCourse(@RequestParam String course) {
        return ResponseEntity.ok(studentService.searchByCourse(course));
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String course) {

        if (id != null && !id.trim().isEmpty()) {
            return ResponseEntity.ok(List.of(studentService.searchByStudentId(id)));
        }
        if (email != null && !email.trim().isEmpty()) {
            return ResponseEntity.ok(List.of(studentService.searchByEmail(email)));
        }
        if (course != null && !course.trim().isEmpty()) {
            return ResponseEntity.ok(studentService.searchByCourse(course));
        }
        return ResponseEntity.ok(studentService.searchStudents(keyword));
    }

    // =========================================================================
    // Requirement 5: Generate a merit list sorted by average score descending
    // =========================================================================

    @GetMapping("/merit-list")
    public ResponseEntity<List<MeritStudentDTO>> getMeritList() {
        return ResponseEntity.ok(studentService.generateMeritList());
    }

    // =========================================================================
    // Requirement 6: Identify students who have the same technical skills
    // =========================================================================

    @GetMapping("/same-skills")
    public ResponseEntity<Map<String, List<StudentResponseDTO>>> getStudentsGroupedBySkill() {
        return ResponseEntity.ok(studentService.groupStudentsBySkill());
    }

    @GetMapping("/same-skill-sets")
    public ResponseEntity<Map<String, List<StudentResponseDTO>>> getStudentsGroupedByIdenticalSkillSet() {
        return ResponseEntity.ok(studentService.groupStudentsByIdenticalSkillSet());
    }

    // =========================================================================
    // Requirement 7: Display students who need improvement in programming (< 50)
    //                but have a mathematics score of at least 75 (>= 75)
    // =========================================================================

    @GetMapping("/programming-improvement-high-math")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsNeedingProgrammingImprovementWithHighMath() {
        return ResponseEntity.ok(studentService.getStudentsNeedingProgrammingImprovementWithHighMath());
    }
}
