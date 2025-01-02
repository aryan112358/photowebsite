package com.photowebsite.repository;

import com.photowebsite.entity.Photo;
import com.photowebsite.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Page<Photo> findByIsPublic(boolean isPublic, Pageable pageable);
    long countByUploadedByUsername(String username);
    long countByUploadedBy(User user);
    
    @Query("SELECT COUNT(l) FROM Photo p JOIN p.likes l WHERE l.user.username = :username")
    long countLikesByUsername(String username);
    
    @Query("SELECT COUNT(c) FROM Photo p JOIN p.comments c WHERE c.user.username = :username")
    long countCommentsByUsername(String username);
    
    Optional<Photo> findByFileName(String fileName);
}