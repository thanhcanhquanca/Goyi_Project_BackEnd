package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.AdPlacementStatus; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "AdPlacements")
@Data
public class AdPlacements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer placementId;

    @ManyToOne
    @JoinColumn(name = "CampaignID", nullable = false)
    private AdCampaigns campaign;

    @ManyToOne
    @JoinColumn(name = "LocationID")
    private AdDisplayLocations location;

    @ManyToOne
    @JoinColumn(name = "VideoID")
    private Videos video;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime displayStart;

    private LocalDateTime displayEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdPlacementStatus status = AdPlacementStatus.INACTIVE; // Giá trị mặc định là INACTIVE

    // Constructor mặc định
    public AdPlacements() {
        this.displayStart = LocalDateTime.now();
    }
}