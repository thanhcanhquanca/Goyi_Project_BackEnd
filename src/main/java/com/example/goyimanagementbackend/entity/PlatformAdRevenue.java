package com.example.goyimanagementbackend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "PlatformAdRevenue")
@Data
public class PlatformAdRevenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer revenueId;

    @ManyToOne
    @JoinColumn(name = "CampaignID", nullable = false)
    private AdminAdCampaigns campaign;

    @Column(nullable = false)
    private Integer totalViews = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cpm;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal revenueAmount;

    @Column(nullable = false)
    private LocalDate revenueDate;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
