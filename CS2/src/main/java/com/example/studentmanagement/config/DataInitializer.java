package com.example.studentmanagement.config;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.*;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(StudentRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            System.out.println(">>> Initializing sample student profiles & skill development records...");

            // Student 1: Excellent performer (Avg: 90.00 >= 85)
            // Skills: Java, Spring Boot, SQL
            Student s1 = new Student("STU101", "Aarav Sharma", "aarav.sharma@example.com", "Computer Science");
            s1.setSkills(new HashSet<>(Arrays.asList("Java", "Spring Boot", "SQL")));
            Map<String, Double> m1 = new HashMap<>();
            m1.put("Mathematics", 92.0);
            m1.put("Programming", 88.0);
            m1.put("Science", 90.0);
            s1.setScores(m1);

            // Student 2: Good performer (Avg: 77.33 between 70 and 84.99)
            // Skills: Python, Machine Learning, SQL
            Student s2 = new Student("STU102", "Diya Patel", "diya.patel@example.com", "Information Technology");
            s2.setSkills(new HashSet<>(Arrays.asList("Python", "Machine Learning", "SQL")));
            Map<String, Double> m2 = new HashMap<>();
            m2.put("Mathematics", 80.0);
            m2.put("Programming", 78.0);
            m2.put("Science", 74.0);
            s2.setScores(m2);

            // Student 3: Satisfactory performer, satisfies Requirement 7 (Programming < 50 and Math >= 75)
            // Skills: Java, SQL, Docker
            Student s3 = new Student("STU103", "Rohan Mehta", "rohan.mehta@example.com", "Computer Science");
            s3.setSkills(new HashSet<>(Arrays.asList("Java", "SQL", "Docker")));
            Map<String, Double> m3 = new HashMap<>();
            m3.put("Mathematics", 82.0);
            m3.put("Programming", 42.0); // < 50 Needs Improvement in Programming
            m3.put("Science", 65.0);
            s3.setScores(m3);

            // Student 4: Satisfactory performer, also satisfies Requirement 7 (Programming < 50 and Math >= 75)
            // Skills: Python, Data Analysis, SQL
            Student s4 = new Student("STU104", "Ananya Iyer", "ananya.iyer@example.com", "Data Science");
            s4.setSkills(new HashSet<>(Arrays.asList("Python", "Data Analysis", "SQL")));
            Map<String, Double> m4 = new HashMap<>();
            m4.put("Mathematics", 76.0);
            m4.put("Programming", 45.0); // < 50 Needs Improvement in Programming
            m4.put("Science", 59.0);
            s4.setScores(m4);

            // Student 5: Satisfactory performer (Avg: 55.67 between 50 and 69.99)
            // Skills: Java, C++, SQL
            Student s5 = new Student("STU105", "Vikram Singh", "vikram.singh@example.com", "Computer Science");
            s5.setSkills(new HashSet<>(Arrays.asList("Java", "C++", "SQL")));
            Map<String, Double> m5 = new HashMap<>();
            m5.put("Mathematics", 60.0);
            m5.put("Programming", 55.0);
            m5.put("Science", 52.0);
            s5.setScores(m5);

            // Student 6: Needs Improvement performer (Avg: 44.33 < 50)
            // Skills: Python, Machine Learning, Cloud Computing
            Student s6 = new Student("STU106", "Sneha Reddy", "sneha.reddy@example.com", "Information Technology");
            s6.setSkills(new HashSet<>(Arrays.asList("Python", "Machine Learning", "Cloud Computing")));
            Map<String, Double> m6 = new HashMap<>();
            m6.put("Mathematics", 45.0);
            m6.put("Programming", 40.0);
            m6.put("Science", 48.0);
            s6.setScores(m6);

            repository.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6));
            System.out.println(">>> Sample student data successfully loaded into database!");
        };
    }
}
