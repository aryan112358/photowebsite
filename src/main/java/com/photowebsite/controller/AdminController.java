package com.photowebsite.controller;

import com.photowebsite.dto.request.UpdateUserRoleRequest;
import com.photowebsite.dto.response.AdminDashboardResponse;
import com.photowebsite.dto.response.UserResponse;
import com.photowebsite.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin", description = "Admin management APIs")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Get admin dashboard statistics")
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllUsers(search, pageable));
    }

    @Operation(summary = "Update user role")
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable Long userId,
            @RequestBody UpdateUserRoleRequest request) {
        return ResponseEntity.ok(adminService.updateUserRole(userId, request));
    }

    @Operation(summary = "Disable user")
    @PostMapping("/users/{userId}/disable")
    public ResponseEntity<UserResponse> disableUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.disableUser(userId));
    }

    @Operation(summary = "Enable user")
    @PostMapping("/users/{userId}/enable")
    public ResponseEntity<UserResponse> enableUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.enableUser(userId));
    }
} 