package com.photowebsite.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {
    private String bio;
    private String location;
    private String website;
    private String socialMediaLinks;
    private String photographyInterests;
    private String cameraGear;
    private MultipartFile avatar;
} 