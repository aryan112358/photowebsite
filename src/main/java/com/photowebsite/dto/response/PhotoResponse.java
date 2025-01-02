package com.photowebsite.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PhotoResponse {
    private Long id;
    private String title;
    private String description;
    private String fileName;
    private String contentType;
    private long size;
    private String category;
    private Boolean isPublic;
    private String uploadedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private byte[] data;
} 