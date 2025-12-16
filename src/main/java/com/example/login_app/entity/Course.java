package com.example.login_app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String category;

    private String instructor;

    @Column(nullable = false)
    private Integer totalModules = 0;

    private Integer completedModules = 0;

    private Double progressPercentage = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;

    private String imageUrl;

    private String difficultyLevel; // Beginner, Intermediate, Advanced

    // Constructors
    public Course() {
        this.createdAt = LocalDateTime.now();
    }

    public Course(String title, String description, String category, String instructor, Integer totalModules) {
        this();
        this.title = title;
        this.description = description;
        this.category = category;
        this.instructor = instructor;
        this.totalModules = totalModules != null ? totalModules : 0;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public Integer getTotalModules() { return totalModules; }
    public void setTotalModules(Integer totalModules) {
        this.totalModules = totalModules != null ? totalModules : 0;
    }

    public Integer getCompletedModules() { return completedModules; }
    public void setCompletedModules(Integer completedModules) {
        this.completedModules = completedModules != null ? completedModules : 0;
        // Auto-calculate progress percentage
        if (this.totalModules > 0) {
            this.progressPercentage = (this.completedModules * 100.0) / this.totalModules;
        }
    }

    public Double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage != null ? progressPercentage : 0.0;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    // Utility method to calculate progress
    public void calculateProgress() {
        if (this.totalModules > 0) {
            this.progressPercentage = (this.completedModules * 100.0) / this.totalModules;
        }
    }
}