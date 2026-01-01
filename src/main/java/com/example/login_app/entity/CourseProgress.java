package com.example.login_app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_progress")
public class CourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // JoinColumn name wahi hona chahiye jo aapke DB table mein hai
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "progress_percentage")
    private double progressPercentage = 0.0;


    @Column(name = "active")
    private boolean isActive = true;

    @Column(name = "completed_modules")
    private Integer completedModules = 0;

    @Column(name = "last_accessed") // SQL schema ke column name ke saath match karne ke liye
    private LocalDateTime updatedAt;

    // ---------------- Constructors ----------------

    // Default Constructor (Hibernate ke liye zaroori hai)
    public CourseProgress() {
    }

    // Standard Constructor (Service layer ke liye)
    public CourseProgress(User user, Course course) {
        this.user = user;
        this.course = course;
        this.progressPercentage = 0.0;
        this.isActive = true;
        this.completedModules = 0;
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

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ---------------- Persistence Hooks ----------------

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}