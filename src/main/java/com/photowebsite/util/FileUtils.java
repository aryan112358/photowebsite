package com.photowebsite.util;

import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileUtils {
    private static final Set<String> ALLOWED_CONTENT_TYPES = new HashSet<>(Arrays.asList(
        "image/jpeg",
        "image/png",
        "image/gif"
    ));

    public static boolean isValidImageFile(MultipartFile file) {
        return file != null && 
               !file.isEmpty() && 
               ALLOWED_CONTENT_TYPES.contains(file.getContentType());
    }

    public static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
} 