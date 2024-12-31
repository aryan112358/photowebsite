package com.photowebsite.service.impl;

import com.photowebsite.dto.request.CreateAlbumRequest;
import com.photowebsite.dto.request.UpdateAlbumRequest;
import com.photowebsite.dto.response.AlbumResponse;
import com.photowebsite.dto.response.PhotoResponse;
import com.photowebsite.entity.Album;
import com.photowebsite.entity.Photo;
import com.photowebsite.entity.User;
import com.photowebsite.repository.AlbumRepository;
import com.photowebsite.repository.UserRepository;
import com.photowebsite.service.AlbumService;
import com.photowebsite.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoService photoService;
    private final UserRepository userRepository;

    public Page<AlbumResponse> getUserAlbums(String username, Pageable pageable, boolean includePrivate) {
        Page<Album> albums = includePrivate ?
            albumRepository.findByUser_Username(username, pageable) :
            albumRepository.findByUser_UsernameAndIsPublicTrue(username, pageable);
        return albums.map(this::mapToAlbumResponse);
    }

    private AlbumResponse mapToAlbumResponse(Album album) {
        Set<PhotoResponse> photoResponses = album.getPhotos().stream()
            .map(photoService::mapToResponse)
            .collect(Collectors.toSet());

        return AlbumResponse.builder()
            .id(album.getId())
            .title(album.getTitle())
            .description(album.getDescription())
            .isPublic(album.isPublic())
            .createdBy(album.getUser().getUsername())
            .photos(photoResponses)
            .createdAt(album.getCreatedAt())
            .updatedAt(album.getUpdatedAt())
            .build();
    }

    @Override
    public AlbumResponse getAlbumById(Long id) {
        Album album = albumRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Album not found"));
        return mapToAlbumResponse(album);
    }

    @Override
    public AlbumResponse createAlbum(CreateAlbumRequest request, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        Album album = Album.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .isPublic(request.isPublic())
            .user(user)
            .build();
            
        return mapToAlbumResponse(albumRepository.save(album));
    }

    @Override
    public void deleteAlbum(Long id, String username) {
        Album album = albumRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Album not found"));
            
        if (!album.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to delete this album");
        }
        
        albumRepository.delete(album);
    }

    @Override
    public AlbumResponse updateAlbum(Long id, UpdateAlbumRequest request, String username) {
        Album album = albumRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Album not found"));
            
        if (!album.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to update this album");
        }
        
        album.setTitle(request.getTitle());
        album.setDescription(request.getDescription());
        album.setPublic(request.isPublic());
        
        return mapToAlbumResponse(albumRepository.save(album));
    }

    @Override
    public AlbumResponse addPhotoToAlbum(Long albumId, Long photoId, String username) {
        Album album = albumRepository.findById(albumId)
            .orElseThrow(() -> new RuntimeException("Album not found"));
            
        if (!album.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to modify this album");
        }
        
        Photo photo = photoService.getPhotoEntity(photoId);
        album.getPhotos().add(photo);
        
        return mapToAlbumResponse(albumRepository.save(album));
    }

    @Override
    public AlbumResponse removePhotoFromAlbum(Long albumId, Long photoId, String username) {
        Album album = albumRepository.findById(albumId)
            .orElseThrow(() -> new RuntimeException("Album not found"));
            
        if (!album.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to modify this album");
        }
        
        album.getPhotos().removeIf(photo -> photo.getId().equals(photoId));
        
        return mapToAlbumResponse(albumRepository.save(album));
    }

    @Override
    public AlbumResponse addPhotosToAlbum(Long albumId, Set<Long> photoIds, String username) {
        Album album = albumRepository.findById(albumId)
            .orElseThrow(() -> new RuntimeException("Album not found"));
            
        if (!album.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to modify this album");
        }
        
        photoIds.forEach(photoId -> {
            Photo photo = photoService.getPhotoEntity(photoId);
            album.getPhotos().add(photo);
        });
        
        return mapToAlbumResponse(albumRepository.save(album));
    }

    @Override
    public AlbumResponse removePhotosFromAlbum(Long albumId, Set<Long> photoIds, String username) {
        Album album = albumRepository.findById(albumId)
            .orElseThrow(() -> new RuntimeException("Album not found"));
            
        if (!album.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to modify this album");
        }
        
        album.getPhotos().removeIf(photo -> photoIds.contains(photo.getId()));
        
        return mapToAlbumResponse(albumRepository.save(album));
    }
} 