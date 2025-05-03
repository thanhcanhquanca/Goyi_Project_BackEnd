package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.AdminActionType; // Thêm import này
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "AdminActivityLogs")
@Data
public class AdminActivityLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "AdminID", nullable = false)
    private Admins admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminActionType actionType;

    @Column(columnDefinition = "TEXT")
    private String actionDetails;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime actionTime;

    // Constructor mặc định
    public AdminActivityLogs() {
        this.actionTime = LocalDateTime.now();
    }
}