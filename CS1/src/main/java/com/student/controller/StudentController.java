package com.student.controller;

import com.student.model.Student;
import com.student.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // Q3.1 - Add a student
    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            String result = service.addStudent(student);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Q3.2 - Retrieve all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    // Q3.3 - Search student by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable int id) {
        Student s = service.getStudentById(id);
        if (s == null) {
            return new ResponseEntity<>("Student not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(s);
    }

    // Q3.4 - Update student scores and skills
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable int id, @RequestBody Student student) {
        try {
            String result = service.updateStudent(id, student);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Q3.5 - Delete a student record
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {
        boolean deleted = service.deleteStudent(id);
        if (deleted) {
            return ResponseEntity.ok("Student with ID " + id + " deleted successfully");
        }
        return new ResponseEntity<>("Student not found with ID: " + id, HttpStatus.NOT_FOUND);
    }

    // Q3.6 - Display merit list (sorted by average score descending)
    @GetMapping("/merit-list")
    public ResponseEntity<List<Student>> getMeritList() {
        return ResponseEntity.ok(service.getMeritList());
    }

    // Q2.6 - Students grouped by shared technical skills
    @GetMapping("/skills/grouped")
    public ResponseEntity<Map<String, List<Student>>> getGroupedBySkill() {
        return ResponseEntity.ok(service.getStudentsGroupedBySkill());
    }

    // Q2.7 - Programming needs improvement but Math >= 75
    @GetMapping("/special-filter")
    public ResponseEntity<List<Student>> getProgrammingNeedsImprovement() {
        return ResponseEntity.ok(service.getProgrammingImprovementStudents());
    }

    // Q2.4 - Search by course
    @GetMapping("/search/course")
    public ResponseEntity<?> searchByCourse(@RequestParam String course) {
        return ResponseEntity.ok(service.searchByCourse(course));
    }

    // Q2.4 - Search by email
    @GetMapping("/search/email")
    public ResponseEntity<?> searchByEmail(@RequestParam String email) {
        Student s = service.searchByEmail(email);
        if (s == null) {
            return new ResponseEntity<>("No student found with email: " + email, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(s);
    }
}
