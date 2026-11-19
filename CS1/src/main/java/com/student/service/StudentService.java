package com.student.service;

import com.student.dao.StudentDAO;
import com.student.model.Student;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentDAO dao = new StudentDAO();

    public String addStudent(Student s) {
        if (s.getProgrammingScore() < 0 || s.getProgrammingScore() > 100 ||
            s.getMathematicsScore() < 0 || s.getMathematicsScore() > 100) {
            throw new IllegalArgumentException("Scores must be between 0 and 100.");
        }
        if (dao.existsById(s.getStudentId())) {
            throw new IllegalArgumentException("Duplicate Student ID: " + s.getStudentId());
        }
        boolean ok = dao.insert(s);
        return ok ? "Student registered successfully" : "Database insertion failed";
    }

    public List<Student> getAllStudents() {
        return dao.findAll();
    }

    public Student getStudentById(int id) {
        return dao.findById(id);
    }

    public String updateStudent(int id, Student s) {
        if (s.getProgrammingScore() < 0 || s.getProgrammingScore() > 100 ||
            s.getMathematicsScore() < 0 || s.getMathematicsScore() > 100) {
            throw new IllegalArgumentException("Scores must be between 0 and 100.");
        }
        boolean ok = dao.update(id, s);
        return ok ? "Student updated successfully" : "Update failed or student ID not found";
    }

    public boolean deleteStudent(int id) {
        return dao.delete(id);
    }

    // Q2.5 - Merit list sorted by average score (descending)
    public List<Student> getMeritList() {
        List<Student> list = new ArrayList<>(dao.findAll());
        list.sort((s1, s2) -> Double.compare(s2.getAverageScore(), s1.getAverageScore()));
        return list;
    }

    // Q2.4 - Search by course
    public List<Student> searchByCourse(String course) {
        return dao.findAll().stream()
                .filter(s -> s.getCourseName().equalsIgnoreCase(course.trim()))
                .collect(Collectors.toList());
    }

    // Q2.4 - Search by email
    public Student searchByEmail(String email) {
        return dao.findAll().stream()
                .filter(s -> s.getEmailAddress().equalsIgnoreCase(email.trim()))
                .findFirst()
                .orElse(null);
    }

    // Q2.6 - Group students who share technical skills
    public Map<String, List<Student>> getStudentsGroupedBySkill() {
        Map<String, List<Student>> map = new HashMap<>();
        for (Student s : dao.findAll()) {
            for (String skill : s.getTechnicalSkills()) {
                map.computeIfAbsent(skill.toLowerCase(), k -> new ArrayList<>()).add(s);
            }
        }
        return map;
    }

    // Q2.7 - Programming < 50 but Mathematics >= 75
    public List<Student> getProgrammingImprovementStudents() {
        return dao.findAll().stream()
                .filter(s -> s.getProgrammingScore() < 50.0 && s.getMathematicsScore() >= 75.0)
                .collect(Collectors.toList());
    }
}
