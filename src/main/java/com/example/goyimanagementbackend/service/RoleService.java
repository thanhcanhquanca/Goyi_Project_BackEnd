package com.example.goyimanagementbackend.service;

import com.example.goyimanagementbackend.dto.RoleDTO;
import com.example.goyimanagementbackend.entity.Permissions;
import com.example.goyimanagementbackend.entity.Roles;
import com.example.goyimanagementbackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(Integer roleId) {
        Roles role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        return toDto(role);
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDto) {
        Roles role = new Roles();
        role.setRoleName(roleDto.getRoleName());
        // Nếu có trường description hoặc các trường khác thì set thêm
        roleRepository.save(role);
        return toDto(role);
    }

    @Override
    public RoleDTO updateRole(Integer roleId, RoleDTO roleDto) {
        Roles role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setRoleName(roleDto.getRoleName());
        roleRepository.save(role);
        return toDto(role);
    }

    @Override
    public void deleteRole(Integer roleId) {
        roleRepository.deleteById(roleId);
    }

    private RoleDTO toDto(Roles role) {
        RoleDTO dto = new RoleDTO();
        dto.setRoleId(role.getRoleId());
        dto.setRoleName(role.getRoleName());
        // Map quyền sang DTO
        if (role.getPermissions() != null) {
            Set<String> permissionNames = role.getPermissions()
                    .stream()
                    .map(Permissions::getPermissionName)
                    .collect(Collectors.toSet());
            dto.setPermissions(permissionNames);
        }
        return dto;
    }
}