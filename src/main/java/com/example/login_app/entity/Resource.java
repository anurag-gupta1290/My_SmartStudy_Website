package com.example.login_app.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String fileType; // PDF, VIDEO, DOCUMENT, etc.
    private String fileUrl;
    private String category;
    private Long downloadCount = 0L;
    private LocalDateTime uploadedAt;

    // Constructors
    public Resource() {
        this.uploadedAt = LocalDateTime.now();
    }

    public Resource(String title, String description, String fileType, String fileUrl, String category) {
        this();
        this.title = title;
        this.description = description;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Long getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Long downloadCount) { this.downloadCount = downloadCount; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}