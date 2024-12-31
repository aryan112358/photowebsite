package com.photowebsite.service;

import com.photowebsite.dto.request.UpdateUserRoleRequest;
import com.photowebsite.dto.response.AdminDashboardResponse;
import com.photowebsite.dto.response.UserResponse;
import com.photowebsite.dto.response.UserStatsResponse;
import com.photowebsite.entity.User;
import com.photowebsite.exception.ResourceNotFoundException;
import com.photowebsite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;
    private final CommentRepository commentRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboardStats() {
        return AdminDashboardResponse.builder()
            .totalUsers(userRepository.count())
            .totalPhotos(photoRepository.count())
            .totalAlbums(albumRepository.count())
            .totalComments(commentRepository.count())
            .totalPayments(paymentRepository.count())
            .build();
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(String search, Pageable pageable) {
        Page<User> users = search != null ?
            userRepository.findByUsernameContainingOrEmailContaining(search, search, pageable) :
            userRepository.findAll(pageable);
            
        return users.map(this::mapToUserResponse);
    }

    @Transactional
    public UserResponse updateUserRole(Long userId, UpdateUserRoleRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRoles(request.getRoles());
        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse disableUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEnabled(false);
        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse enableUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEnabled(true);
        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .roles(user.getRoles())
            .enabled(user.isEnabled())
            .createdAt(user.getCreatedAt())
            .lastLogin(user.getLastLogin())
            .build();
    }

    public UserStatsResponse getUserStats(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
        return UserStatsResponse.builder()
            .username(username)
            .photoCount(photoRepository.countByUploadedBy(user))
            // ... other fields ...
            .build();
    }
} 