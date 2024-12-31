package com.photowebsite.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Set;

@Data
public class CreateAlbumRequest {
    @NotBlank(message = "Album title is required")
    private String title;
    
    private String description;
    private boolean isPublic = true;
    private Set<Long> photoIds;
} 