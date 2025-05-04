package com.example.goyimanagementbackend.repository;

import com.example.goyimanagementbackend.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByRoleName(String roleName);

    @Query(value = "SELECT p.permission_name FROM goyi_management_db.permissions p " +
            "JOIN goyi_management_db.role_permissions rp ON p.permission_id = rp.permissionid " +
            "WHERE rp.roleid = :roleId", nativeQuery = true)
    List<String> findPermissionNamesByRoleId(@Param("roleId") Integer roleId);
}
