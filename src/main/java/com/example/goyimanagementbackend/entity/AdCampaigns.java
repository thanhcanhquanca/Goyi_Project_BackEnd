package com.example.goyimanagementbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.example.goyimanagementbackend.eNum.CampaignStatus; // Thêm import này

@Entity
@Table(name = "AdCampaigns")
@Data
public class AdCampaigns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer campaignId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Column(nullable = false, length = 255)
    private String campaignName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal budget;

    @Column(nullable = false)
    private Integer duration;

    private Integer targetViews = 0;

    private Integer targetSubscriptions = 0;

    private Integer viewsAchieved = 0;

    private Integer subscriptionsAchieved = 0;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CampaignStatus status = CampaignStatus.PENDING;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public AdCampaigns() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}