package com.example.studentmanagement.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudentResponseDTO {

    private Long id;
    private String studentId;
    private String name;
    private String email;
    private String course;
    private Set<String> skills = new HashSet<>();
    private Map<String, Double> scores = new HashMap<>();

    // Requirement 1: Calculate each student's average score
    private Double averageScore;

    // Requirement 2: Performance category based on average
    // Excellent (>= 85), Good (70-84.99), Satisfactory (50-69.99), Needs Improvement (< 50)
    private String performanceCategory;

    // Requirement 3: Identify the stronger subject for each student
    private String strongerSubject;

    public StudentResponseDTO() {
    }

    public StudentResponseDTO(Long id, String studentId, String name, String email, String course,
                              Set<String> skills, Map<String, Double> scores,
                              Double averageScore, String performanceCategory, String strongerSubject) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.course = course;
        if (skills != null) this.skills = skills;
        if (scores != null) this.scores = scores;
        this.averageScore = averageScore;
        this.performanceCategory = performanceCategory;
        this.strongerSubject = strongerSubject;
    }

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

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public String getPerformanceCategory() {
        return performanceCategory;
    }

    public void setPerformanceCategory(String performanceCategory) {
        this.performanceCategory = performanceCategory;
    }

    public String getStrongerSubject() {
        return strongerSubject;
    }

    public void setStrongerSubject(String strongerSubject) {
        this.strongerSubject = strongerSubject;
    }
}
