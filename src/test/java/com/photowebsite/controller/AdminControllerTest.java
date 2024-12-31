package com.photowebsite.controller;

import com.photowebsite.dto.request.UpdateUserRoleRequest;
import com.photowebsite.dto.response.AdminDashboardResponse;
import com.photowebsite.dto.response.UserResponse;
import com.photowebsite.service.AdminService;
import com.photowebsite.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDashboardStats_Success() throws Exception {
        AdminDashboardResponse response = AdminDashboardResponse.builder()
            .totalUsers(10)
            .totalPhotos(100)
            .build();

        when(adminService.getDashboardStats()).thenReturn(response);

        mockMvc.perform(get("/api/admin/dashboard"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalUsers").value(10))
            .andExpect(jsonPath("$.totalPhotos").value(100));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_Success() throws Exception {
        UserResponse userResponse = UserResponse.builder()
            .id(1L)
            .username("testuser")
            .build();
        Page<UserResponse> page = new PageImpl<>(Collections.singletonList(userResponse));

        when(adminService.getAllUsers(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/admin/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].username").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getDashboardStats_Forbidden() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserRole_Success() throws Exception {
        UpdateUserRoleRequest request = new UpdateUserRoleRequest();
        request.setRoles(new HashSet<Role>(Collections.singletonList(Role.ROLE_ADMIN)));

        UserResponse response = UserResponse.builder()
            .id(1L)
            .username("testuser")
            .roles(request.getRoles())
            .build();

        when(adminService.updateUserRole(any(), any())).thenReturn(response);

        mockMvc.perform(put("/api/admin/users/1/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roles\":[\"ROLE_ADMIN\"]}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("testuser"));
    }
} 