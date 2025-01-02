package com.photowebsite.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import com.photowebsite.entity.User;
import com.photowebsite.repository.PhotoRepository;
import com.photowebsite.repository.UserRepository;
import com.photowebsite.repository.AlbumRepository;
import com.photowebsite.service.PhotoService;
import com.photowebsite.dto.request.PhotoRequest;
import com.photowebsite.dto.response.PhotoResponse;
import com.photowebsite.entity.Photo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;

    @Override
    public PhotoResponse addPhoto(PhotoRequest request, String username) {
        try {
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            Photo photo = Photo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .fileName(request.getFile().getOriginalFilename())
                .contentType(request.getFile().getContentType())
                .size(request.getFile().getSize())
                .data(request.getFile().getBytes())
                .uploadedBy(user)
                .isPublic(request.isPublic())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();

            photo = photoRepository.save(photo);
            return mapToResponse(photo);
        } catch (Exception e) {
            log.error("Failed to add photo", e);
            throw new RuntimeException("Failed to add photo", e);
        }
    }

    @Override
    public PhotoResponse mapToResponse(Photo photo) {
        return PhotoResponse.builder()
            .id(photo.getId())
            .title(photo.getTitle())
            .description(photo.getDescription())
            .fileName(photo.getFileName())
            .contentType(photo.getContentType())
            .size(photo.getSize())
            .category(photo.getCategory())
            .isPublic(photo.isPublic())
            .uploadedBy(photo.getUploadedBy().getUsername())
            .createdAt(photo.getCreatedAt())
            .updatedAt(photo.getUpdatedAt())
            .build();
    }

    @Override
    public void deletePhoto(Long id, String username) {
        Photo photo = photoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Photo not found"));
            
        if (!photo.getUploadedBy().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to delete this photo");
        }
        
        photoRepository.delete(photo);
    }

    @Override
    public Page<PhotoResponse> getAllPhotos(Pageable pageable, boolean publicOnly) {
        Page<Photo> photos = publicOnly ? 
            photoRepository.findByIsPublic(true, pageable) : 
            photoRepository.findAll(pageable);
            
        return photos.map(this::mapToResponse);
    }

    @Override
    public PhotoResponse getPhotoById(Long id) {
        Photo photo = photoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Photo not found"));
        return mapToResponse(photo);
    }

    @Override
    public long countUserPhotos(String username) {
        return photoRepository.countByUploadedByUsername(username);
    }

    @Override
    public long countUserAlbums(String username) {
        return albumRepository.countByUser_Username(username);
    }

    @Override
    public long countUserPhotoLikes(String username) {
        return photoRepository.countLikesByUsername(username);
    }

    @Override
    public long countUserPhotoComments(String username) {
        return photoRepository.countCommentsByUsername(username);
    }

    @Override
    public Photo getPhotoEntity(Long id) {
        return photoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Photo not found"));
    }

    @Override
    public PhotoResponse getPhotoByFileName(String fileName) {
        Photo photo = photoRepository.findByFileName(fileName)
            .orElseThrow(() -> new RuntimeException("Photo not found"));
        return mapToResponse(photo);
    }
} 