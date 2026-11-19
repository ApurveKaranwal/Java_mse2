package com.example.studentsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class StudentRequestDTO {

    @NotBlank(message = "Student name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String department;

    private Integer semester;

    @NotNull(message = "Academic score is required")
    @Min(value = 0, message = "Academic score cannot be less than 0")
    @Max(value = 100, message = "Academic score cannot exceed 100")
    private Double academicScore;

    private List<String> skills = new ArrayList<>();

    public StudentRequestDTO() {
    }

    public StudentRequestDTO(String name, String email, String department, Integer semester, Double academicScore, List<String> skills) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.semester = semester;
        this.academicScore = academicScore;
        this.skills = skills;
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

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
