package com.photowebsite.service;

import com.photowebsite.dto.request.UpdateUserRoleRequest;
import com.photowebsite.dto.response.AdminDashboardResponse;
import com.photowebsite.dto.response.UserResponse;
import com.photowebsite.entity.User;
import com.photowebsite.entity.Role;
import com.photowebsite.exception.ResourceNotFoundException;
import com.photowebsite.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private AdminService adminService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setRoles(new HashSet<Role>(Collections.singletonList(Role.ROLE_USER)));
    }

    @Test
    void getDashboardStats_Success() {
        when(userRepository.count()).thenReturn(10L);
        when(photoRepository.count()).thenReturn(100L);
        when(albumRepository.count()).thenReturn(20L);
        when(commentRepository.count()).thenReturn(200L);
        when(paymentRepository.count()).thenReturn(50L);

        AdminDashboardResponse response = adminService.getDashboardStats();

        assertEquals(10L, response.getTotalUsers());
        assertEquals(100L, response.getTotalPhotos());
        assertEquals(20L, response.getTotalAlbums());
        assertEquals(200L, response.getTotalComments());
        assertEquals(50L, response.getTotalPayments());
    }

    @Test
    void getAllUsers_Success() {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(testUser));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<UserResponse> response = adminService.getAllUsers(null, Pageable.unpaged());

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(testUser.getUsername(), response.getContent().get(0).getUsername());
    }

    @Test
    void updateUserRole_Success() {
        Set<Role> newRoles = new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN));
        UpdateUserRoleRequest request = new UpdateUserRoleRequest();
        request.setRoles(newRoles);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = adminService.updateUserRole(1L, request);

        assertNotNull(response);
        assertEquals(testUser.getUsername(), response.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserRole_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateUserRoleRequest request = new UpdateUserRoleRequest();
        request.setRoles(new HashSet<>());

        assertThrows(ResourceNotFoundException.class, () -> 
            adminService.updateUserRole(1L, request)
        );
    }

    @Test
    void disableUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = adminService.disableUser(1L);

        assertNotNull(response);
        assertFalse(response.isEnabled());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void enableUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = adminService.enableUser(1L);

        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }
} 