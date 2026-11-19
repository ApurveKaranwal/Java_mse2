package com.student.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student {
    private int studentId;
    private String studentName;
    private String emailAddress;
    private String courseName;
    private double programmingScore;
    private double mathematicsScore;
    private List<String> technicalSkills;

    public Student() {
        this.technicalSkills = new ArrayList<>();
    }

    public Student(int studentId, String studentName, String emailAddress,
                   String courseName, double programmingScore,
                   double mathematicsScore, List<String> technicalSkills) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.emailAddress = emailAddress;
        this.courseName = courseName;
        this.programmingScore = programmingScore;
        this.mathematicsScore = mathematicsScore;
        this.technicalSkills = technicalSkills != null ? technicalSkills : new ArrayList<>();
    }

    public double getAverageScore() {
        return (this.programmingScore + this.mathematicsScore) / 2.0;
    }

    public String getPerformanceCategory() {
        double avg = getAverageScore();
        if (avg >= 85.0) return "Excellent";
        if (avg >= 70.0) return "Good";
        if (avg >= 50.0) return "Satisfactory";
        return "Needs Improvement";
    }

    public String getStrongerSubject() {
        if (this.programmingScore > this.mathematicsScore) return "Programming";
        if (this.mathematicsScore > this.programmingScore) return "Mathematics";
        return "Equal";
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public double getProgrammingScore() { return programmingScore; }
    public void setProgrammingScore(double programmingScore) { this.programmingScore = programmingScore; }

    public double getMathematicsScore() { return mathematicsScore; }
    public void setMathematicsScore(double mathematicsScore) { this.mathematicsScore = mathematicsScore; }

    public List<String> getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(List<String> technicalSkills) { this.technicalSkills = technicalSkills; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return studentId == student.studentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + studentId +
                ", name='" + studentName + '\'' +
                ", email='" + emailAddress + '\'' +
                ", course='" + courseName + '\'' +
                ", programming=" + programmingScore +
                ", math=" + mathematicsScore +
                ", avg=" + String.format("%.2f", getAverageScore()) +
                ", performance='" + getPerformanceCategory() + '\'' +
                ", strongerSubject='" + getStrongerSubject() + '\'' +
                ", skills=" + technicalSkills +
                '}';
    }
}
