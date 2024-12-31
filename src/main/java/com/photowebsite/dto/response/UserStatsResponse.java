package com.photowebsite.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatsResponse {
    private String username;
    private long photoCount;
    private long albumCount;
    private long commentCount;
    private long salesCount;
    private double totalEarnings;
} 