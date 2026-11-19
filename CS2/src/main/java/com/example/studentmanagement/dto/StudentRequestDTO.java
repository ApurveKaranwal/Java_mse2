package com.example.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudentRequestDTO {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Course is required")
    private String course;

    private Set<String> skills = new HashSet<>();

    private Map<String, Double> scores = new HashMap<>();

    public StudentRequestDTO() {
    }

    public StudentRequestDTO(String studentId, String name, String email, String course, Set<String> skills, Map<String, Double> scores) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.course = course;
        if (skills != null) this.skills = skills;
        if (scores != null) this.scores = scores;
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
}
