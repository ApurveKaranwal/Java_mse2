package com.example.studentsystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for Requirement 4: "Update student scores and skills"
 */
public class ScoreSkillsUpdateDTO {

    @NotNull(message = "Academic score cannot be null")
    @Min(value = 0, message = "Academic score must be at least 0")
    @Max(value = 100, message = "Academic score cannot exceed 100")
    private Double academicScore;

    private List<String> skills = new ArrayList<>();

    public ScoreSkillsUpdateDTO() {
    }

    public ScoreSkillsUpdateDTO(Double academicScore, List<String> skills) {
        this.academicScore = academicScore;
        this.skills = skills;
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
