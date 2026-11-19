package com.example.studentmanagement.dto;

public class MeritStudentDTO {

    private int rank;
    private String studentId;
    private String name;
    private String course;
    private Double averageScore;
    private String performanceCategory;
    private String strongerSubject;

    public MeritStudentDTO() {
    }

    public MeritStudentDTO(int rank, String studentId, String name, String course,
                           Double averageScore, String performanceCategory, String strongerSubject) {
        this.rank = rank;
        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.averageScore = averageScore;
        this.performanceCategory = performanceCategory;
        this.strongerSubject = strongerSubject;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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
