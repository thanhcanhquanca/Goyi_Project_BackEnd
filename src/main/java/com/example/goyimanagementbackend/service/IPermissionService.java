package com.example.goyimanagementbackend.service;

import com.example.goyimanagementbackend.dto.PermissionDTO;
import java.util.List;

public interface IPermissionService {
    List<PermissionDTO> getAllPermissions();
    PermissionDTO getPermissionById(Integer permissionId);
    PermissionDTO createPermission(PermissionDTO permissionDto);
    PermissionDTO updatePermission(Integer permissionId, PermissionDTO permissionDto);
    void deletePermission(Integer permissionId);
}
