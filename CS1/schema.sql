-- ================================================================
-- Student Profile and Skill Development Management System
-- ================================================================

CREATE DATABASE IF NOT EXISTS student_profile_db;
USE student_profile_db;

DROP TABLE IF EXISTS students;

CREATE TABLE students (
    student_id        INT           PRIMARY KEY,
    student_name      VARCHAR(100)  NOT NULL,
    email_address     VARCHAR(150)  NOT NULL UNIQUE,
    course_name       VARCHAR(100)  NOT NULL,
    programming_score DOUBLE        NOT NULL,
    mathematics_score DOUBLE        NOT NULL,
    technical_skills  VARCHAR(255)  NOT NULL
);

-- Sample data covering all 4 performance categories
INSERT INTO students VALUES
(101, 'Apurve Sharma', 'apurve@kiet.edu',  'B.Tech CSE',    92.0, 96.0, 'Java, Spring Boot, MySQL'),
(102, 'Rohan Verma',   'rohan@kiet.edu',   'B.Tech CSE-AI', 82.0, 78.0, 'Python, Machine Learning, Java'),
(103, 'Priya Singh',   'priya@kiet.edu',   'B.Tech IT',     58.0, 64.0, 'Java, HTML, CSS'),
(104, 'Amit Kumar',    'amit@kiet.edu',    'B.Tech CSE-DS', 40.0, 48.0, 'C, C++, Data Structures'),
(105, 'Neha Patel',    'neha@kiet.edu',    'B.Tech CS',     42.0, 88.0, 'Java, SQL, Git'),
(106, 'Vikas Gupta',   'vikas@kiet.edu',   'B.Tech CSE',    88.0, 84.0, 'Java, Spring Boot, Docker');
