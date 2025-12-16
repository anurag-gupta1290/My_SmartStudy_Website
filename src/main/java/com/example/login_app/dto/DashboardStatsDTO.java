package com.example.login_app.dto;

public class DashboardStatsDTO {
    private Integer activeCourses;
    private Double overallProgress;
    private Double studyTimeHours;
    private String aiTutorStatus;

    public DashboardStatsDTO() {}

    public DashboardStatsDTO(Integer activeCourses, Double overallProgress, Double studyTimeHours, String aiTutorStatus) {
        this.activeCourses = activeCourses;
        this.overallProgress = overallProgress;
        this.studyTimeHours = studyTimeHours;
        this.aiTutorStatus = aiTutorStatus;
    }

    // Getters & Setters
    public Integer getActiveCourses() { return activeCourses; }
    public void setActiveCourses(Integer activeCourses) { this.activeCourses = activeCourses; }

    public Double getOverallProgress() { return overallProgress; }
    public void setOverallProgress(Double overallProgress) { this.overallProgress = overallProgress; }

    public Double getStudyTimeHours() { return studyTimeHours; }
    public void setStudyTimeHours(Double studyTimeHours) { this.studyTimeHours = studyTimeHours; }

    public String getAiTutorStatus() { return aiTutorStatus; }
    public void setAiTutorStatus(String aiTutorStatus) { this.aiTutorStatus = aiTutorStatus; }
}
