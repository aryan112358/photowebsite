package com.photowebsite.dto.request;

import com.photowebsite.entity.Role;
import lombok.Data;
import java.util.Set;

@Data
public class UpdateUserRoleRequest {
    private Set<Role> roles;
} 