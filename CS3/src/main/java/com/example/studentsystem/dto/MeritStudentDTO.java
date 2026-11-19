package com.example.studentsystem.dto;

import java.util.List;

/**
 * DTO for Requirement 6: "Display the merit list"
 * Demonstrates Java Collections data manipulation, ranking, and standing calculation.
 */
public class MeritStudentDTO {

    private int rank;
    private Long id;
    private String name;
    private String email;
    private String department;
    private Double academicScore;
    private String grade;
    private List<String> skills;
    private String academicStanding;

    public MeritStudentDTO() {
    }

    public MeritStudentDTO(int rank, Long id, String name, String email, String department,
                           Double academicScore, String grade, List<String> skills, String academicStanding) {
        this.rank = rank;
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.academicScore = academicScore;
        this.grade = grade;
        this.skills = skills;
        this.academicStanding = academicStanding;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

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
        this.skills = skills;
    }

    public String getAcademicStanding() {
        return academicStanding;
    }

    public void setAcademicStanding(String academicStanding) {
        this.academicStanding = academicStanding;
    }
}
