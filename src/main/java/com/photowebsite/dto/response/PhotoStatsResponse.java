package com.photowebsite.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoStatsResponse {
    private long totalPhotos;
    private long totalAlbums;
    private long totalLikes;
    private long totalComments;
} 