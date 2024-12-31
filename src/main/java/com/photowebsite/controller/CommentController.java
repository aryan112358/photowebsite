package com.photowebsite.controller;

import com.photowebsite.dto.request.CommentRequest;
import com.photowebsite.dto.response.CommentResponse;
import com.photowebsite.service.CommentService;
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

@Tag(name = "Comments", description = "Comment management APIs")
@RestController
@RequestMapping("/api/photos/{photoId}/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Add comment to photo")
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long photoId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(commentService.addComment(photoId, request, userDetails.getUsername()));
    }

    @Operation(summary = "Get photo comments")
    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getPhotoComments(
            @PathVariable Long photoId,
            Pageable pageable) {
        return ResponseEntity.ok(commentService.getPhotoComments(photoId, pageable));
    }

    @Operation(summary = "Update comment")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long photoId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(commentService.updateComment(photoId, commentId, request, userDetails.getUsername()));
    }

    @Operation(summary = "Delete comment")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long photoId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteComment(photoId, commentId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
} 