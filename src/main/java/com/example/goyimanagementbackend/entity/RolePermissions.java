package com.example.goyimanagementbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "RolePermissions")
@Data
public class RolePermissions {
    @Id
    @ManyToOne
    @JoinColumn(name = "RoleID", nullable = false)
    private Roles role;

    @Id
    @ManyToOne
    @JoinColumn(name = "PermissionID", nullable = false)
    private Permissions permission;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
