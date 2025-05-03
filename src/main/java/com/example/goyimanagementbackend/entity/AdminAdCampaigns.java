package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.AdminAdStatus;
import com.example.goyimanagementbackend.eNum.AdminAdTargetLocation;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "AdminAdCampaigns")
@Data
public class AdminAdCampaigns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer campaignId;

    @ManyToOne
    @JoinColumn(name = "AdminID", nullable = false)
    private Admins admin;

    @Column(nullable = false, length = 255)
    private String campaignTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String campaignContent;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminAdTargetLocation targetLocation;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminAdStatus status = AdminAdStatus.INACTIVE; // Giá trị mặc định

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Constructor mặc định
    public AdminAdCampaigns() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}