package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.MeritStudentDTO;
import com.example.studentmanagement.dto.StudentRequestDTO;
import com.example.studentmanagement.dto.StudentResponseDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.DuplicateStudentIdException;
import com.example.studentmanagement.exception.InvalidScoreException;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // =========================================================================
    // Core CRUD Operations with Requirement 8 Validation
    // =========================================================================

    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        // Requirement 8: Prevent duplicate student IDs
        if (studentRepository.existsByStudentId(requestDTO.getStudentId())) {
            throw new DuplicateStudentIdException(
                    "Student ID '" + requestDTO.getStudentId() + "' already exists. Duplicate IDs are not allowed."
            );
        }

        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateStudentIdException(
                    "Email '" + requestDTO.getEmail() + "' is already registered with another student."
            );
        }

        // Requirement 8: Validate scores between 0 and 100
        validateScores(requestDTO.getScores());

        Student student = new Student();
        student.setStudentId(requestDTO.getStudentId().trim());
        student.setName(requestDTO.getName().trim());
        student.setEmail(requestDTO.getEmail().trim().toLowerCase());
        student.setCourse(requestDTO.getCourse().trim());

        if (requestDTO.getSkills() != null) {
            Set<String> cleanSkills = requestDTO.getSkills().stream()
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .map(String::trim)
                    .collect(Collectors.toSet());
            student.setSkills(cleanSkills);
        }

        if (requestDTO.getScores() != null) {
            student.setScores(new HashMap<>(requestDTO.getScores()));
        }

        Student saved = studentRepository.save(student);
        return mapToResponseDTO(saved);
    }

    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with database ID: " + id));

        // Requirement 8: Prevent duplicate student IDs upon update
        if (studentRepository.existsByStudentIdAndIdNot(requestDTO.getStudentId(), id)) {
            throw new DuplicateStudentIdException(
                    "Student ID '" + requestDTO.getStudentId() + "' already exists for another record."
            );
        }

        if (studentRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new DuplicateStudentIdException(
                    "Email '" + requestDTO.getEmail() + "' already exists for another record."
            );
        }

        // Requirement 8: Validate scores
        validateScores(requestDTO.getScores());

        student.setStudentId(requestDTO.getStudentId().trim());
        student.setName(requestDTO.getName().trim());
        student.setEmail(requestDTO.getEmail().trim().toLowerCase());
        student.setCourse(requestDTO.getCourse().trim());

        Set<String> cleanSkills = (requestDTO.getSkills() == null) ? new HashSet<>() :
                requestDTO.getSkills().stream()
                        .filter(s -> s != null && !s.trim().isEmpty())
                        .map(String::trim)
                        .collect(Collectors.toSet());
        student.setSkills(cleanSkills);

        student.setScores(requestDTO.getScores() != null ? new HashMap<>(requestDTO.getScores()) : new HashMap<>());

        Student updated = studentRepository.save(student);
        return mapToResponseDTO(updated);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with database ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with database ID: " + id));
        return mapToResponseDTO(student);
    }

    // =========================================================================
    // Requirement 1: Calculate each student's average score
    // =========================================================================
    public double calculateAverageScore(Map<String, Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }
        double average = scores.values().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return BigDecimal.valueOf(average)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    // =========================================================================
    // Requirement 2: Assign a performance category based on the average
    //   - 85 or above: Excellent
    //   - 70–84.99: Good
    //   - 50–69.99: Satisfactory
    //   - Below 50: Needs Improvement
    // =========================================================================
    public String assignPerformanceCategory(double averageScore) {
        if (averageScore >= 85.0) {
            return "Excellent";
        } else if (averageScore >= 70.0) {
            return "Good";
        } else if (averageScore >= 50.0) {
            return "Satisfactory";
        } else {
            return "Needs Improvement";
        }
    }

    // =========================================================================
    // Requirement 3: Identify the stronger subject for each student
    // =========================================================================
    public String identifyStrongerSubject(Map<String, Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return "No Scores";
        }

        // Use Java Collections & Streams to find max entry
        return scores.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey() + " (" + e.getValue() + ")")
                .orElse("No Scores");
    }

    // =========================================================================
    // Requirement 4: Search for students by ID, email or course
    // =========================================================================
    @Transactional(readOnly = true)
    public StudentResponseDTO searchByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId.trim())
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO searchByEmail(String email) {
        return studentRepository.findByEmail(email.trim().toLowerCase())
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public List<StudentResponseDTO> searchByCourse(String course) {
        return studentRepository.findByCourseIgnoreCase(course.trim()).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentResponseDTO> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentRepository.searchByKeyword(keyword.trim()).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // Requirement 5: Generate a merit list sorted by average score in descending order
    // =========================================================================
    @Transactional(readOnly = true)
    public List<MeritStudentDTO> generateMeritList() {
        List<Student> students = studentRepository.findAll();

        // Sort descending by average score using Java Collections / Streams
        List<StudentResponseDTO> responseDTOs = students.stream()
                .map(this::mapToResponseDTO)
                .sorted(Comparator.comparingDouble(StudentResponseDTO::getAverageScore).reversed()
                        .thenComparing(StudentResponseDTO::getName))
                .toList();

        // Assign ranks to generate the final merit list
        List<MeritStudentDTO> meritList = new ArrayList<>();
        int rank = 1;
        for (StudentResponseDTO dto : responseDTOs) {
            meritList.add(new MeritStudentDTO(
                    rank++,
                    dto.getStudentId(),
                    dto.getName(),
                    dto.getCourse(),
                    dto.getAverageScore(),
                    dto.getPerformanceCategory(),
                    dto.getStrongerSubject()
            ));
        }
        return meritList;
    }

    // =========================================================================
    // Requirement 6: Identify students who have the same technical skills
    // =========================================================================

    /**
     * Group students by individual technical skill (e.g., "Java" -> [Student1, Student3])
     */
    @Transactional(readOnly = true)
    public Map<String, List<StudentResponseDTO>> groupStudentsBySkill() {
        List<Student> students = studentRepository.findAll();
        Map<String, List<StudentResponseDTO>> skillMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Student student : students) {
            StudentResponseDTO dto = mapToResponseDTO(student);
            for (String skill : student.getSkills()) {
                skillMap.computeIfAbsent(skill, k -> new ArrayList<>()).add(dto);
            }
        }
        return skillMap;
    }

    /**
     * Group students who share the exact same set of technical skills
     */
    @Transactional(readOnly = true)
    public Map<String, List<StudentResponseDTO>> groupStudentsByIdenticalSkillSet() {
        List<Student> students = studentRepository.findAll();
        Map<String, List<StudentResponseDTO>> identicalSetMap = new LinkedHashMap<>();

        for (Student student : students) {
            StudentResponseDTO dto = mapToResponseDTO(student);
            // Sort skills so sets are canonical regardless of insertion order
            String key = student.getSkills().stream()
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .collect(Collectors.joining(", "));
            if (key.isEmpty()) {
                key = "No Skills Specified";
            }
            identicalSetMap.computeIfAbsent(key, k -> new ArrayList<>()).add(dto);
        }
        return identicalSetMap;
    }

    // =========================================================================
    // Requirement 7: Display students who need improvement in programming
    //                but have a mathematics score of at least 75
    //   - "needs improvement in programming": Programming score < 50
    //   - "mathematics score of at least 75": Mathematics score >= 75
    // =========================================================================
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsNeedingProgrammingImprovementWithHighMath() {
        return studentRepository.findAll().stream()
                .filter(student -> {
                    Double progScore = findSubjectScore(student.getScores(), "programming");
                    Double mathScore = findSubjectScore(student.getScores(), "math");

                    // Condition: Programming < 50 AND Mathematics >= 75
                    return progScore != null && progScore < 50.0 &&
                           mathScore != null && mathScore >= 75.0;
                })
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // Requirement 8: Validate scores between 0 and 100
    // =========================================================================
    public void validateScores(Map<String, Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            String subject = entry.getKey();
            Double score = entry.getValue();

            if (score == null) {
                throw new InvalidScoreException("Score for subject '" + subject + "' cannot be null.");
            }
            if (score < 0.0 || score > 100.0) {
                throw new InvalidScoreException(
                        "Score for subject '" + subject + "' must be between 0 and 100. Received: " + score
                );
            }
        }
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    public StudentResponseDTO mapToResponseDTO(Student student) {
        double average = calculateAverageScore(student.getScores());
        String category = assignPerformanceCategory(average);
        String strongerSubject = identifyStrongerSubject(student.getScores());

        return new StudentResponseDTO(
                student.getId(),
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getCourse(),
                student.getSkills(),
                student.getScores(),
                average,
                category,
                strongerSubject
        );
    }

    private Double findSubjectScore(Map<String, Double> scores, String keyword) {
        if (scores == null || scores.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            if (entry.getKey().toLowerCase().contains(keyword.toLowerCase())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
