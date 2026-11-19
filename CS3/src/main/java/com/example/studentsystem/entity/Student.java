package com.example.studentsystem.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Student Entity
 * Represents student personal information, academic scores, and skill interests.
 * Mapped to MySQL database using Spring Data JPA.
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Personal Information ---
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 100)
    private String department;

    private Integer semester;

    // --- Academic Scores ---
    @Column(nullable = false)
    private Double academicScore;

    @Column(length = 20)
    private String grade;

    // --- Skill Interests (Java Collection mapped via JPA) ---
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill_name")
    private List<String> skills = new ArrayList<>();

    // Default Constructor (required by JPA)
    public Student() {
    }

    // Parameterized Constructor
    public Student(String name, String email, String department, Integer semester, Double academicScore, List<String> skills) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.semester = semester;
        this.academicScore = academicScore;
        this.skills = skills != null ? skills : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Double getAcademicScore() {
        return academicScore;
    }

    public void setAcademicScore(Double academicScore) {
        this.academicScore = academicScore;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills != null ? skills : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                ", academicScore=" + academicScore +
                ", grade='" + grade + '\'' +
                ", skills=" + skills +
                '}';
    }
}
