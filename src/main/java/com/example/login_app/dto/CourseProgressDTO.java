package com.example.login_app.dto;


public class CourseProgressDTO {
    private Long courseId;
    private String courseTitle;
    private String description;
    private String category;
    private Integer completedModules;
    private Integer totalModules;
    private Double progressPercentage;
    private Boolean isActive;

    // Constructors
    public CourseProgressDTO() {}

    // Getters and Setters
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getCompletedModules() { return completedModules; }
    public void setCompletedModules(Integer completedModules) { this.completedModules = completedModules; }

    public Integer getTotalModules() { return totalModules; }
    public void setTotalModules(Integer totalModules) { this.totalModules = totalModules; }

    public Double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Double progressPercentage) { this.progressPercentage = progressPercentage; }

    public Boolean getActive() { return isActive; }
    public void setActive(Boolean active) { isActive = active; }
}