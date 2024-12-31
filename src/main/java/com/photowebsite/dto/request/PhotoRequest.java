package com.photowebsite.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

@Data
public class PhotoRequest {
    private String title;
    private String description;
    private String category;
    private boolean isPublic;
    private BigDecimal price;
    private MultipartFile file;
} 