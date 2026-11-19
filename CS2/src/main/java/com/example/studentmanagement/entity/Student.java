package com.example.studentmanagement.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Requirement 8: Prevent duplicate student IDs
    @Column(name = "student_id", nullable = false, unique = true, length = 50)
    private String studentId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "course", nullable = false, length = 100)
    private String course;

    // Requirement 6: Technical skills stored using Java Collections (Set ensures uniqueness)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "student_skills",
            joinColumns = @JoinColumn(name = "student_id")
    )
    @Column(name = "skill", nullable = false)
    private Set<String> skills = new HashSet<>();

    // Requirement 1, 2, 3, 7, 8: Academic scores stored in a Java Map<Subject, Score>
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "student_scores",
            joinColumns = @JoinColumn(name = "student_id")
    )
    @MapKeyColumn(name = "subject_name")
    @Column(name = "score", nullable = false)
    private Map<String, Double> scores = new HashMap<>();

    public Student() {
    }

    public Student(String studentId, String name, String email, String course) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.course = course;
    }

    public Student(Long id, String studentId, String name, String email, String course, Set<String> skills, Map<String, Double> scores) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.course = course;
        if (skills != null) {
            this.skills = skills;
        }
        if (scores != null) {
            this.scores = scores;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills != null ? skills : new HashSet<>();
    }

    public Map<String, Double> getScores() {
        return scores;
    }

    public void setScores(Map<String, Double> scores) {
        this.scores = scores != null ? scores : new HashMap<>();
    }

    public void addSkill(String skill) {
        if (skill != null && !skill.trim().isEmpty()) {
            this.skills.add(skill.trim());
        }
    }

    public void addScore(String subject, Double score) {
        if (subject != null && score != null) {
            this.scores.put(subject.trim(), score);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", skills=" + skills +
                ", scores=" + scores +
                '}';
    }
}
