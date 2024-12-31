package com.photowebsite.repository;

import com.photowebsite.entity.Comment;
import com.photowebsite.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPhotoId(Long photoId, Pageable pageable);
    long countByPhotoId(Long photoId);
    long countByCreatedBy(String username);
    long countByPhotoUploadedBy(User uploadedBy);
    List<Comment> findByPhotoUploadedBy(User uploadedBy);
    Page<Comment> findByPhotoUploadedBy(User uploadedBy, Pageable pageable);
} 