package com.photowebsite.controller;

import com.photowebsite.dto.request.PhotoRequest;
import com.photowebsite.dto.response.PhotoResponse;
import com.photowebsite.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Photos", description = "Photo management APIs")
@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @Operation(summary = "Get all photos")
    @GetMapping
    public ResponseEntity<Page<PhotoResponse>> getAllPhotos(
            @Parameter(description = "Filter public photos only") 
            @RequestParam(defaultValue = "false") boolean publicOnly,
            Pageable pageable) {
        return ResponseEntity.ok(photoService.getAllPhotos(pageable, publicOnly));
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
}
