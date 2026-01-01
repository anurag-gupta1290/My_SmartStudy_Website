package com.example.login_app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_progress")
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private CourseVideo video;

    private Boolean completed = false;
    private LocalDateTime completedAt;

    // Getters and Setters
}
