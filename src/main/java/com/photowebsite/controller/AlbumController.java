package com.photowebsite.controller;

import com.photowebsite.dto.request.CreateAlbumRequest;
import com.photowebsite.dto.request.UpdateAlbumRequest;
import com.photowebsite.dto.response.AlbumResponse;
import com.photowebsite.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Albums", description = "Album management APIs")
@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "Create new album")
    @PostMapping
    public ResponseEntity<AlbumResponse> createAlbum(
            @Valid @RequestBody CreateAlbumRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(albumService.createAlbum(request, userDetails.getUsername()));
    }

    @Operation(summary = "Get album by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> getAlbum(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.getAlbumById(id));
    }

    @Operation(summary = "Update album")
    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponse> updateAlbum(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAlbumRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(albumService.updateAlbum(id, request, userDetails.getUsername()));
    }

    @Operation(summary = "Delete album")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        albumService.deleteAlbum(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get user's albums")
    @GetMapping("/user/{username}")
    public ResponseEntity<Page<AlbumResponse>> getUserAlbums(
            @PathVariable String username,
            @RequestParam(defaultValue = "false") boolean includePrivate,
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        boolean isOwner = userDetails.getUsername().equals(username);
        return ResponseEntity.ok(albumService.getUserAlbums(username, pageable, isOwner && includePrivate));
    }

    @Operation(summary = "Add photos to album")
    @PostMapping("/{id}/photos")
    public ResponseEntity<AlbumResponse> addPhotosToAlbum(
            @PathVariable Long id,
            @RequestBody Set<Long> photoIds,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(albumService.addPhotosToAlbum(id, photoIds, userDetails.getUsername()));
    }

    @Operation(summary = "Remove photos from album")
    @DeleteMapping("/{id}/photos")
    public ResponseEntity<AlbumResponse> removePhotosFromAlbum(
            @PathVariable Long id,
            @RequestBody Set<Long> photoIds,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(albumService.removePhotosFromAlbum(id, photoIds, userDetails.getUsername()));
    }
} 