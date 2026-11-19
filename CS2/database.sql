-- =====================================================================
-- Database Schema for Student Profile & Skill Development Management System
-- Problem Statement: Q2. a)
-- =====================================================================

-- 1. Create Database if not exists
CREATE DATABASE IF NOT EXISTS student_mgmt_db;
USE student_mgmt_db;

-- 2. Drop existing tables if needed (in proper foreign-key order)
DROP TABLE IF EXISTS student_scores;
DROP TABLE IF EXISTS student_skills;
DROP TABLE IF EXISTS students;

-- 3. Create 'students' table
-- Stores student personal information and course
-- Requirement 8: Prevent duplicate student IDs (UNIQUE constraint on student_id)
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    course VARCHAR(100) NOT NULL
);

-- 4. Create 'student_skills' table
-- Stores technical skill interests using 1-to-many relationship
-- Requirement 6: Identify students who have the same technical skills
CREATE TABLE student_skills (
    student_id BIGINT NOT NULL,
    skill VARCHAR(100) NOT NULL,
    PRIMARY KEY (student_id, skill),
    CONSTRAINT fk_skills_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- 5. Create 'student_scores' table
-- Stores academic subject scores
-- Requirement 8: Validate scores between 0 and 100 (CHECK constraint)
CREATE TABLE student_scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    score DOUBLE NOT NULL CHECK (score >= 0.0 AND score <= 100.0),
    CONSTRAINT uk_student_subject UNIQUE (student_id, subject_name),
    CONSTRAINT fk_scores_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- =====================================================================
-- Sample Seed Data
-- =====================================================================

-- Insert Students
INSERT INTO students (id, student_id, name, email, course) VALUES
(1, 'STU101', 'Aarav Sharma', 'aarav.sharma@example.com', 'Computer Science'),
(2, 'STU102', 'Diya Patel', 'diya.patel@example.com', 'Information Technology'),
(3, 'STU103', 'Rohan Mehta', 'rohan.mehta@example.com', 'Computer Science'),
(4, 'STU104', 'Ananya Iyer', 'ananya.iyer@example.com', 'Data Science'),
(5, 'STU105', 'Vikram Singh', 'vikram.singh@example.com', 'Computer Science'),
(6, 'STU106', 'Sneha Reddy', 'sneha.reddy@example.com', 'Information Technology');

-- Insert Skills (Requirement 6: Same technical skills testing)
-- Aarav, Rohan, and Vikram have "Java" and "SQL"
-- Diya and Sneha have "Python" and "Machine Learning"
-- Ananya has "Python" and "Data Analysis"
INSERT INTO student_skills (student_id, skill) VALUES
(1, 'Java'), (1, 'Spring Boot'), (1, 'SQL'),
(2, 'Python'), (2, 'Machine Learning'), (2, 'SQL'),
(3, 'Java'), (3, 'SQL'), (3, 'Docker'),
(4, 'Python'), (4, 'Data Analysis'), (4, 'SQL'),
(5, 'Java'), (5, 'C++'), (5, 'SQL'),
(6, 'Python'), (6, 'Machine Learning'), (6, 'Cloud Computing');

-- Insert Scores
-- Notice:
-- Aarav Sharma: Math=92, Programming=88, Science=90 -> Avg=90.00 (Excellent)
-- Diya Patel: Math=80, Programming=78, Science=74 -> Avg=77.33 (Good)
-- Rohan Mehta: Math=82, Programming=42, Science=65 -> Avg=63.00 (Satisfactory, Programming < 50 and Math >= 75 - Requirement 7!)
-- Ananya Iyer: Math=76, Programming=45, Science=59 -> Avg=60.00 (Satisfactory, Programming < 50 and Math >= 75 - Requirement 7!)
-- Vikram Singh: Math=60, Programming=55, Science=52 -> Avg=55.67 (Satisfactory)
-- Sneha Reddy: Math=45, Programming=40, Science=48 -> Avg=44.33 (Needs Improvement)
INSERT INTO student_scores (student_id, subject_name, score) VALUES
-- Aarav (Excellent)
(1, 'Mathematics', 92.0),
(1, 'Programming', 88.0),
(1, 'Science', 90.0),

-- Diya (Good)
(2, 'Mathematics', 80.0),
(2, 'Programming', 78.0),
(2, 'Science', 74.0),

-- Rohan (Req 7 match: Math >= 75 and Programming < 50)
(3, 'Mathematics', 82.0),
(3, 'Programming', 42.0),
(3, 'Science', 65.0),

-- Ananya (Req 7 match: Math >= 75 and Programming < 50)
(4, 'Mathematics', 76.0),
(4, 'Programming', 45.0),
(4, 'Science', 59.0),

-- Vikram (Satisfactory)
(5, 'Mathematics', 60.0),
(5, 'Programming', 55.0),
(5, 'Science', 52.0),

-- Sneha (Needs Improvement)
(6, 'Mathematics', 45.0),
(6, 'Programming', 40.0),
(6, 'Science', 48.0);
