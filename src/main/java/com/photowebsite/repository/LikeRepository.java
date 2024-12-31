package com.photowebsite.repository;

import com.photowebsite.entity.Like;
import com.photowebsite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPhotoUploadedBy(User uploadedBy);
    List<Like> findByPhotoUploadedBy(User uploadedBy);
    
    boolean existsByPhoto_IdAndUser_Username(Long photoId, String username);
    void deleteByPhoto_IdAndUser_Username(Long photoId, String username);
    
    long countByPhotoId(Long photoId);
} 