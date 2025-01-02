package com.photowebsite.controller;

import com.photowebsite.service.FileStorageService;
import com.photowebsite.dto.request.PhotoRequest;
import com.photowebsite.dto.response.PhotoResponse;
import com.photowebsite.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.http.HttpStatus;
import com.photowebsite.entity.Photo;

@Tag(name = "Photos", description = "Photo management APIs")
@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoService photoService;
    private final FileStorageService fileStorageService;

    @Autowired
    public PhotoController(FileStorageService fileStorageService,PhotoService photoService) {
        this.fileStorageService = fileStorageService;
        this.photoService = photoService;
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<byte[]> getPhotoByFileName(@PathVariable String fileName) {
        PhotoResponse photo = photoService.getPhotoByFileName(fileName);
        
        if (photo == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (!photo.getIsPublic()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || 
                authentication instanceof AnonymousAuthenticationToken) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(photo.getContentType()))
            .body(photo.getData());
    }

    @Operation(summary = "Get all photos")
    @GetMapping
    public ResponseEntity<Page<PhotoResponse>> getAllPhotos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(photoService.getAllPhotos(pageable, true));
    }

    @Operation(summary = "Get photo by ID")
    @GetMapping("/{id}")
    public ResponseEntity<PhotoResponse> getPhotoById(@PathVariable Long id) {
        return ResponseEntity.ok(photoService.getPhotoById(id));
    }

    @Operation(summary = "Upload new photo")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoResponse> addPhoto(
            @Valid @ModelAttribute PhotoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        PhotoResponse response = photoService.addPhoto(request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete photo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        photoService.deletePhoto(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        PhotoResponse photo = photoService.getPhotoById(id);
        
        if (photo == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (!photo.getIsPublic()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || 
                authentication instanceof AnonymousAuthenticationToken) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(photo.getContentType()))
            .body(photo.getData());
    }
}
