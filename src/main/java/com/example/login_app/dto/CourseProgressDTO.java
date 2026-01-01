package com.example.login_app.dto;

public class CourseProgressDTO {
    private Long courseId;
    private String courseName; // JS ke sath match karne ke liye 'courseName' rakha hai
    private Double progressPercentage;
    private String category;
    private Boolean isActive;

    // Default Constructor
    public CourseProgressDTO() {}

    // Main Constructor (Jo Service layer use karegi)
    public CourseProgressDTO(Long courseId, String courseName, Double progressPercentage) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.progressPercentage = progressPercentage;
    }

    // Constructor with Active Status
    public CourseProgressDTO(Long courseId, String courseName, Double progressPercentage, Boolean isActive) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.progressPercentage = progressPercentage;
        this.isActive = isActive;
    }

    // Getters and Setters
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Double progressPercentage) { this.progressPercentage = progressPercentage; }

    public Boolean getActive() { return isActive; }
    public void setActive(Boolean active) { isActive = active; }
}