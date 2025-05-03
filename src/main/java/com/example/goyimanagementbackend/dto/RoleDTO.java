package com.example.goyimanagementbackend.dto;

import lombok.Data;
import java.util.Set;

@Data
public class RoleDTO {
    private Integer roleId;
    private String roleName;
    private Set<String> permissions; // Tên các quyền (nếu có)
}