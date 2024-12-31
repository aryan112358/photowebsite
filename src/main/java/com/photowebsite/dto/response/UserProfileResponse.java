package com.photowebsite.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private String username;
    private String email;
    private String avatarUrl;
    private String bio;
    private String location;
    private String website;
    private String socialMediaLinks;
    private String photographyInterests;
    private String cameraGear;
    private PhotoStatsResponse stats;
} 