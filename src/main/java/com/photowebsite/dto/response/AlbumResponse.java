package com.photowebsite.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class AlbumResponse {
    private Long id;
    private String title;
    private String description;
    private boolean isPublic;
    private String createdBy;
    private Set<PhotoResponse> photos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 