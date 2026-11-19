package com.example.studentsystem.service;

import com.example.studentsystem.dto.MeritStudentDTO;
import com.example.studentsystem.dto.ScoreSkillsUpdateDTO;
import com.example.studentsystem.dto.StudentRequestDTO;
import com.example.studentsystem.entity.Student;
import com.example.studentsystem.exception.BadRequestException;
import com.example.studentsystem.exception.ResourceNotFoundException;
import com.example.studentsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // =========================================================================
    // 1. ADD A STUDENT
    // =========================================================================
    @Override
    public Student addStudent(StudentRequestDTO requestDTO) {
        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BadRequestException("Student with email '" + requestDTO.getEmail() + "' already exists!");
        }

        validateScore(requestDTO.getAcademicScore());

        Student student = new Student();
        student.setName(requestDTO.getName().trim());
        student.setEmail(requestDTO.getEmail().trim().toLowerCase());
        student.setDepartment(requestDTO.getDepartment());
        student.setSemester(requestDTO.getSemester());
        student.setAcademicScore(requestDTO.getAcademicScore());

        // Business Rule: Calculate and assign grade based on academic score
        student.setGrade(calculateGrade(requestDTO.getAcademicScore()));

        // Java Collections: Clean, deduplicate and sanitize skills
        student.setSkills(processSkillsCollection(requestDTO.getSkills()));

        return studentRepository.save(student);
    }

    // =========================================================================
    // 2. RETRIEVE ALL STUDENTS
    // =========================================================================
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // =========================================================================
    // 3. SEARCH FOR A STUDENT BY ID
    // =========================================================================
    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    // =========================================================================
    // 4. UPDATE STUDENT SCORES AND SKILLS
    // =========================================================================
    @Override
    public Student updateScoresAndSkills(Long id, ScoreSkillsUpdateDTO updateDTO) {
        Student student = getStudentById(id);

        validateScore(updateDTO.getAcademicScore());

        // Update score and re-evaluate grade
        student.setAcademicScore(updateDTO.getAcademicScore());
        student.setGrade(calculateGrade(updateDTO.getAcademicScore()));

        // Java Collections: Update and deduplicate skills
        student.setSkills(processSkillsCollection(updateDTO.getSkills()));

        return studentRepository.save(student);
    }

    @Override
    public Student updateStudentFull(Long id, StudentRequestDTO requestDTO) {
        Student student = getStudentById(id);

        if (!student.getEmail().equalsIgnoreCase(requestDTO.getEmail()) &&
                studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BadRequestException("Email '" + requestDTO.getEmail() + "' is already in use by another student!");
        }

        validateScore(requestDTO.getAcademicScore());

        student.setName(requestDTO.getName().trim());
        student.setEmail(requestDTO.getEmail().trim().toLowerCase());
        student.setDepartment(requestDTO.getDepartment());
        student.setSemester(requestDTO.getSemester());
        student.setAcademicScore(requestDTO.getAcademicScore());
        student.setGrade(calculateGrade(requestDTO.getAcademicScore()));
        student.setSkills(processSkillsCollection(requestDTO.getSkills()));

        return studentRepository.save(student);
    }

    // =========================================================================
    // 5. DELETE A STUDENT RECORD
    // =========================================================================
    @Override
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    // =========================================================================
    // 6. DISPLAY THE MERIT LIST (Java Collections & Data Manipulation)
    // =========================================================================
    @Override
    public List<MeritStudentDTO> getMeritList() {
        List<Student> allStudents = studentRepository.findAll();

        if (allStudents.isEmpty()) {
            return Collections.emptyList();
        }

        // Meaningful Data Manipulation using Java Collections & Streams:
        // Sort students in descending order based on academicScore
        List<Student> sortedStudents = allStudents.stream()
                .sorted(Comparator.comparingDouble(Student::getAcademicScore).reversed()
                        .thenComparing(Student::getName))
                .collect(Collectors.toList());

        // Transform into Merit List DTO with sequential rankings and academic standing
        List<MeritStudentDTO> meritList = new ArrayList<>();
        int currentRank = 1;

        for (Student student : sortedStudents) {
            String standing = determineAcademicStanding(student.getAcademicScore(), currentRank);

            MeritStudentDTO meritItem = new MeritStudentDTO(
                    currentRank++,
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    student.getDepartment(),
                    student.getAcademicScore(),
                    student.getGrade(),
                    new ArrayList<>(student.getSkills()),
                    standing
            );
            meritList.add(meritItem);
        }

        return meritList;
    }

    // =========================================================================
    // BUSINESS RULES & DATA MANIPULATION HELPERS
    // =========================================================================

    /**
     * Business Rule: Calculate letter grade from numeric academic score.
     */
    private String calculateGrade(Double score) {
        if (score == null) return "N/A";
        if (score >= 90.0) return "A+ (Outstanding)";
        if (score >= 80.0) return "A (Excellent)";
        if (score >= 70.0) return "B (Good)";
        if (score >= 60.0) return "C (Average)";
        if (score >= 50.0) return "D (Pass)";
        return "F (Fail)";
    }

    /**
     * Business Rule: Determine academic standing based on score and merit rank.
     */
    private String determineAcademicStanding(Double score, int rank) {
        if (rank == 1) return "Topper - First Rank with Distinction";
        if (score >= 85.0) return "Distinction";
        if (score >= 65.0) return "First Division";
        if (score >= 50.0) return "Second Division";
        return "Needs Academic Improvement";
    }

    /**
     * Java Collections Data Manipulation:
     * Cleans whitespace, trims, deduplicates using LinkedHashSet while preserving insertion order.
     */
    private List<String> processSkillsCollection(List<String> rawSkills) {
        if (rawSkills == null || rawSkills.isEmpty()) {
            return new ArrayList<>();
        }

        // Using LinkedHashSet to remove duplicates while preserving order
        Set<String> cleanSkillSet = new LinkedHashSet<>();
        for (String skill : rawSkills) {
            if (skill != null && !skill.trim().isEmpty()) {
                cleanSkillSet.add(skill.trim());
            }
        }

        return new ArrayList<>(cleanSkillSet);
    }

    /**
     * Business Rule: Validate academic score range [0.0 - 100.0]
     */
    private void validateScore(Double score) {
        if (score == null || score < 0.0 || score > 100.0) {
            throw new BadRequestException("Academic score must be between 0.0 and 100.0");
        }
    }
}
