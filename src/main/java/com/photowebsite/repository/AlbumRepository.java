package com.photowebsite.repository;

import com.photowebsite.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("SELECT COUNT(a) FROM Album a WHERE a.user.username = :username")
    long countByUser_Username(String username);
    
    @Query("SELECT a FROM Album a WHERE a.user.username = :username")
    Page<Album> findByUser_Username(String username, Pageable pageable);
    
    @Query("SELECT a FROM Album a WHERE a.user.username = :username AND a.isPublic = true")
    Page<Album> findByUser_UsernameAndIsPublicTrue(String username, Pageable pageable);
} 