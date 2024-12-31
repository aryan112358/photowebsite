package com.photowebsite.service;

import org.springframework.web.multipart.MultipartFile;
import com.photowebsite.dto.request.PhotoRequest;
import com.photowebsite.dto.response.PhotoResponse;
import com.photowebsite.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhotoService {
    PhotoResponse addPhoto(PhotoRequest request, String username);
    PhotoResponse mapToResponse(Photo photo);
    void deletePhoto(Long id, String username);
    Page<PhotoResponse> getAllPhotos(Pageable pageable, boolean publicOnly);
    PhotoResponse getPhotoById(Long id);
    long countUserPhotos(String username);
    long countUserAlbums(String username);
    long countUserPhotoLikes(String username);
    long countUserPhotoComments(String username);
    Photo getPhotoEntity(Long id);
}