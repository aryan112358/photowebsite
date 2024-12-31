package com.photowebsite.dto.request;

import lombok.Data;

@Data
public class UpdateAlbumRequest {
    private String title;
    private String description;
    private boolean isPublic;
} 