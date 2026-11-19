# Student Profile and Skill Development Management System

A production-ready **Java Spring Boot** application that maintains student personal information, academic scores, and technical skill interests. It applies domain business rules, leverages the **Java Collections Framework** (`List`, `Set`, `Map`, `Streams`), and persists data to a **MySQL database**.

---

## 📋 Problem Statement & Requirements Mapping

| # | Requirement | Implementation in System |
|---|---|---|
| **1** | **Calculate each student's average score** | Uses `scores.values().stream().mapToDouble(...).average()` rounded to 2 decimal places in `StudentService.calculateAverageScore()`. |
| **2** | **Assign performance category based on average** | `StudentService.assignPerformanceCategory()` assigns:<br>&bull; **&ge; 85.0**: `Excellent`<br>&bull; **70.0 – 84.99**: `Good`<br>&bull; **50.0 – 69.99**: `Satisfactory`<br>&bull; **&lt; 50.0**: `Needs Improvement` |
| **3** | **Identify the stronger subject for each student** | Uses Java Collections comparator `scores.entrySet().stream().max(Map.Entry.comparingByValue())` in `StudentService.identifyStrongerSubject()`. |
| **4** | **Search for students by ID, email or course** | Provides dedicated search methods and REST endpoints: `/api/students/student-id/{id}`, `/api/students/search/email`, `/api/students/search/course`, and `/api/students/search?keyword=...`. |
| **5** | **Generate merit list sorted by average score descending** | Uses Java Streams `.sorted(Comparator.comparingDouble(StudentResponseDTO::getAverageScore).reversed())` and assigns ranks (1, 2, 3...) in `StudentService.generateMeritList()`. |
| **6** | **Identify students who have the same technical skills** | Groups students by individual technical skill (`Map<String, List<StudentResponseDTO>>`) and identical skill sets in `StudentService.groupStudentsBySkill()`. |
| **7** | **Display students needing improvement in programming (< 50) with Math >= 75** | Filters student collections using condition `progScore < 50.0 && mathScore >= 75.0` in `StudentService.getStudentsNeedingProgrammingImprovementWithHighMath()`. |
| **8** | **Prevent duplicate student IDs and validate scores (0–100)** | Enforces DB constraints (`UNIQUE(student_id)`, `CHECK(score BETWEEN 0 AND 100)`) + Spring Service validation throwing `DuplicateStudentIdException` and `InvalidScoreException`. |

---

## 🗄️ Database Design (MySQL)

File: `database.sql` (located in root directory)

```sql
CREATE DATABASE IF NOT EXISTS student_mgmt_db;
USE student_mgmt_db;

-- 1. Students Table
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    course VARCHAR(100) NOT NULL
);

-- 2. Technical Skills Table (Java Set collection mapping)
CREATE TABLE student_skills (
    student_id BIGINT NOT NULL,
    skill VARCHAR(100) NOT NULL,
    PRIMARY KEY (student_id, skill),
    CONSTRAINT fk_skills_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- 3. Academic Scores Table (Java Map collection mapping)
CREATE TABLE student_scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    score DOUBLE NOT NULL CHECK (score >= 0.0 AND score <= 100.0),
    CONSTRAINT uk_student_subject UNIQUE (student_id, subject_name),
    CONSTRAINT fk_scores_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);
```

---

## 🚀 How to Run in IntelliJ IDEA

### Step 1: Open the Project
1. Open **IntelliJ IDEA**.
2. Click **File** &rarr; **Open...**
3. Browse to this directory: `c:\Users\Apurve\Desktop\java\m2` and click **OK**.
4. If prompted, select **Open as Project** and choose **Trust Project**.
5. IntelliJ will automatically detect `pom.xml` and download/resolve dependencies.

### Step 2: Configure Project SDK
1. Go to **File** &rarr; **Project Structure...** (or press `Ctrl + Alt + Shift + S`).
2. Under **Project**, set **SDK** to your installed JDK (e.g. JDK 17, 21, or 26).
3. Set **Language level** to `17` or `21` (matches `pom.xml`).
4. Click **Apply** and **OK**.

### Step 3: Configure Database Credentials
Open `src/main/resources/application.properties` and verify your MySQL root password:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_mgmt_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```
> **Tip (Zero Setup Option):** If MySQL is not running or you want to test immediately without MySQL, run with the `h2` profile by adding `--spring.profiles.active=h2` in IntelliJ Run Configuration (or changing `application.properties`).

### Step 4: Run the Application
1. In the Project Explorer, expand:
   `src/main/java/com/example/studentmanagement/`
2. Right-click on **`StudentManagementApplication.java`**.
3. Select **Run 'StudentManagementApplication'** (or press `Shift + F10`).
4. Watch the Run console until you see:
   `Started StudentManagementApplication in ... seconds`

### Step 5: Open the Interactive Web Dashboard
Open your web browser and navigate to:
```
http://localhost:8080
```
From this dashboard, you can:
- View all students with auto-calculated averages, performance categories, and stronger subjects.
- Search students live by ID, Email, or Course.
- View the sorted Merit List with ranks.
- View students grouped by technical skills.
- View students needing improvement in programming but strong in math.
- Add new students with instant duplicate ID and score validation.

### Step 6: Run Tests in IntelliJ
1. Navigate to: `src/test/java/com/example/studentmanagement/StudentServiceTest.java`
2. Right-click `StudentServiceTest` and click **Run 'StudentServiceTest'** (or `Ctrl + Shift + F10`).
3. All 9 test cases will run and show green checkmarks.

---

## 🌐 REST API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/students` | Get all students with calculated metrics |
| `POST` | `/api/students` | Register student (validates ID uniqueness & scores 0-100) |
| `GET` | `/api/students/{id}` | Get student by DB ID |
| `GET` | `/api/students/student-id/{studentId}` | Search student by Student ID (Req 4) |
| `GET` | `/api/students/search/email?email=...` | Search student by Email (Req 4) |
| `GET` | `/api/students/search/course?course=...` | Search students by Course (Req 4) |
| `GET` | `/api/students/search?keyword=...` | Flexible keyword search (Req 4) |
| `GET` | `/api/students/merit-list` | Get merit list sorted descending by average score (Req 5) |
| `GET` | `/api/students/same-skills` | Group students by individual technical skill (Req 6) |
| `GET` | `/api/students/same-skill-sets` | Group students sharing identical skill set (Req 6) |
| `GET` | `/api/students/programming-improvement-high-math` | Filter: Programming < 50 and Math >= 75 (Req 7) |
| `PUT` | `/api/students/{id}` | Update student details |
| `DELETE` | `/api/students/{id}` | Delete student record |
