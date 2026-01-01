package com.example.login_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments") // Pehle yahan 'user_id' tha, use 'assignments' kiya gaya hai
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    // Database mein 'user_id' column hai, isliye JoinColumn ka name 'user_id' hi rahega
    @ManyToOne(fetch = FetchType.EAGER) // LAZY ki jagah EAGER karein taaki data turant load ho
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String status = "Pending";

    @Column(name = "due_date") // Database column name se match karne ke liye
    private LocalDateTime dueDate;

    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private Integer maxPoints = 100;
    private Integer earnedPoints;

    // Constructor initialization
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Assignment() {}

    public Assignment(String title, String description, User user, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters (Keep as they are)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public Integer getMaxPoints() { return maxPoints; }
    public void setMaxPoints(Integer maxPoints) { this.maxPoints = maxPoints; }
    public Integer getEarnedPoints() { return earnedPoints; }
    public void setEarnedPoints(Integer earnedPoints) { this.earnedPoints = earnedPoints; }
}