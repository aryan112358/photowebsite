package com.photowebsite.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    private String avatarUrl;
    private String bio;
    private String location;
    private String website;
    private String socialMediaLinks;
    
    @Column(name = "photography_interests")
    private String photographyInterests;
    
    @Column(name = "camera_gear")
    private String cameraGear;
    
    private LocalDateTime lastUpdated;

    @PreUpdate
    @PrePersist
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
} 