package com.photowebsite.service;

import com.photowebsite.dto.request.CommentRequest;
import com.photowebsite.dto.response.CommentResponse;
import com.photowebsite.entity.Comment;
import com.photowebsite.entity.Photo;
import com.photowebsite.exception.ResourceNotFoundException;
import com.photowebsite.exception.UnauthorizedException;
import com.photowebsite.repository.CommentRepository;
import com.photowebsite.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PhotoRepository photoRepository;
    private final UserProfileService userProfileService;

    @Transactional
    public CommentResponse addComment(Long photoId, CommentRequest request, String username) {
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPhoto(photo);
        comment.setCreatedBy(username);

        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getPhotoComments(Long photoId, Pageable pageable) {
        if (!photoRepository.existsById(photoId)) {
            throw new ResourceNotFoundException("Photo not found");
        }

        return commentRepository.findByPhotoId(photoId, pageable)
            .map(this::mapToResponse);
    }

    @Transactional
    public CommentResponse updateComment(Long photoId, Long commentId, CommentRequest request, String username) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getPhoto().getId().equals(photoId)) {
            throw new ResourceNotFoundException("Comment not found for this photo");
        }

        if (!comment.getCreatedBy().equals(username)) {
            throw new UnauthorizedException("You don't have permission to update this comment");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return mapToResponse(updatedComment);
    }

    @Transactional
    public void deleteComment(Long photoId, Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getPhoto().getId().equals(photoId)) {
            throw new ResourceNotFoundException("Comment not found for this photo");
        }

        if (!comment.getCreatedBy().equals(username)) {
            throw new UnauthorizedException("You don't have permission to delete this comment");
        }

        commentRepository.delete(comment);
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .createdBy(comment.getCreatedBy())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .user(userProfileService.getProfile(comment.getCreatedBy()))
            .build();
    }
} 