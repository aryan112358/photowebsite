package com.photowebsite.service;

import com.photowebsite.dto.response.AlbumResponse;
import com.photowebsite.dto.request.CreateAlbumRequest;
import com.photowebsite.dto.request.UpdateAlbumRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Set;

public interface AlbumService {
    Page<AlbumResponse> getUserAlbums(String username, Pageable pageable, boolean includePrivate);
    AlbumResponse getAlbumById(Long id);
    AlbumResponse createAlbum(CreateAlbumRequest request, String username);
    void deleteAlbum(Long id, String username);
    AlbumResponse updateAlbum(Long id, UpdateAlbumRequest request, String username);
    AlbumResponse addPhotoToAlbum(Long albumId, Long photoId, String username);
    AlbumResponse removePhotoFromAlbum(Long albumId, Long photoId, String username);
    AlbumResponse addPhotosToAlbum(Long albumId, Set<Long> photoIds, String username);
    AlbumResponse removePhotosFromAlbum(Long albumId, Set<Long> photoIds, String username);
} 