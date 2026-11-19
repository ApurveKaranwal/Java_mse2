package com.example.studentsystem.service;

import com.example.studentsystem.dto.MeritStudentDTO;
import com.example.studentsystem.dto.ScoreSkillsUpdateDTO;
import com.example.studentsystem.dto.StudentRequestDTO;
import com.example.studentsystem.entity.Student;

import java.util.List;

public interface StudentService {

    // 1. Add a student
    Student addStudent(StudentRequestDTO requestDTO);

    // 2. Retrieve all students
    List<Student> getAllStudents();

    // 3. Search for a student by ID
    Student getStudentById(Long id);

    // 4. Update student scores and skills
    Student updateScoresAndSkills(Long id, ScoreSkillsUpdateDTO updateDTO);

    // Full student update helper
    Student updateStudentFull(Long id, StudentRequestDTO requestDTO);

    // 5. Delete a student record
    void deleteStudent(Long id);

    // 6. Display the merit list
    List<MeritStudentDTO> getMeritList();
}
