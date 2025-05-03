package com.example.goyimanagementbackend.service;

import com.example.goyimanagementbackend.dto.RoleDTO;
import java.util.List;

public interface IRoleService {
    List<RoleDTO> getAllRoles();
    RoleDTO getRoleById(Integer roleId);
    RoleDTO createRole(RoleDTO roleDto);
    RoleDTO updateRole(Integer roleId, RoleDTO roleDto);
    void deleteRole(Integer roleId);
}
