package com.photowebsite.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PhotoRequest {
    private String title;
    private String description;
    private String category;
    private MultipartFile file;
    private boolean isPublic = true;
} 