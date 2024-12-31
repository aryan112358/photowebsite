package com.photowebsite.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PhotoResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private LocalDateTime uploadDate;
    private String uploadedBy;
    private boolean isPublic;
} 