package com.example.login_app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_progress")
public class CourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private double progressPercentage;
    private boolean isActive = true;

    private Integer completedModules = 0;
    private Integer totalModules = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ---------------- Constructors ----------------

    public CourseProgress() {
    }

    public CourseProgress(User user, Course course) {
        this.user = user;
        this.course = course;
        this.progressPercentage = 0.0;
        this.isActive = true;
        this.completedModules = 0;
        this.totalModules = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public CourseProgress(User user, Course course, double progressPercentage) {
        this.user = user;
        this.course = course;
        this.progressPercentage = progressPercentage;
        this.isActive = true;
        this.completedModules = 0;
        this.totalModules = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ---------------- Getters & Setters ----------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(double progressPercentage) { this.progressPercentage = progressPercentage; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }

    public Integer getCompletedModules() { return completedModules; }
    public void setCompletedModules(Integer completedModules) { this.completedModules = completedModules; }

    public Integer getTotalModules() { return totalModules; }
    public void setTotalModules(Integer totalModules) { this.totalModules = totalModules; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ---------------- Convenience Methods ----------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
