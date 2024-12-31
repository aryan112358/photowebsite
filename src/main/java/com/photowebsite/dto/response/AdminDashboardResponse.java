package com.photowebsite.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardResponse {
    private long totalUsers;
    private long totalPhotos;
    private long totalAlbums;
    private long totalComments;
    private long totalPayments;
} 