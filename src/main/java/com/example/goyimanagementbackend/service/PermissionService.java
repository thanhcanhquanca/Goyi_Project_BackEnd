package com.example.goyimanagementbackend.service;

import com.example.goyimanagementbackend.dto.PermissionDTO;
import com.example.goyimanagementbackend.entity.Permissions;
import com.example.goyimanagementbackend.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService implements IPermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PermissionDTO getPermissionById(Integer permissionId) {
        Permissions permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));
        return toDto(permission);
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDto) {
        Permissions permission = new Permissions();
        permission.setPermissionName(permissionDto.getPermissionName());
        permissionRepository.save(permission);
        return toDto(permission);
    }

    @Override
    public PermissionDTO updatePermission(Integer permissionId, PermissionDTO permissionDto) {
        Permissions permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));
        permission.setPermissionName(permissionDto.getPermissionName());
        permissionRepository.save(permission);
        return toDto(permission);
    }

    @Override
    public void deletePermission(Integer permissionId) {
        permissionRepository.deleteById(permissionId);
    }

    private PermissionDTO toDto(Permissions permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setPermissionId(permission.getPermissionId());
        dto.setPermissionName(permission.getPermissionName());
        return dto;
    }
}