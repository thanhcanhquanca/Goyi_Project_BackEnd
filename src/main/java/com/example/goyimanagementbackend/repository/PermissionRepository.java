package com.example.goyimanagementbackend.repository;

import com.example.goyimanagementbackend.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permissions, Integer> {
    Optional<Permissions> findByPermissionName(String permissionName);
}
