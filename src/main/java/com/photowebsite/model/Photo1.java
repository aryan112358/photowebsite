//package com.photowebsite.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import java.time.LocalDateTime;
//
//@Data
//@Entity
//@Table(name = "photos")
//public class Photo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String title;
//
//    @Column(nullable = false)
//    private String fileName;
//
//    @Column(nullable = false)
//    private String contentType;
//
//    private String description;
//
//    private Long fileSize;
//
//    @Column(nullable = false)
//    private LocalDateTime uploadDate;
//
//    @Column(nullable = false)
//    private String uploadedBy;
//
//    private String category;
//
//    private boolean isPublic = true;
//
//    @PrePersist
//    protected void onCreate() {
//        uploadDate = LocalDateTime.now();
//    }
//}