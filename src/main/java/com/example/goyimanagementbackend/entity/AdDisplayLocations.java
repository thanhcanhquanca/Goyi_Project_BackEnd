package com.example.goyimanagementbackend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "AdDisplayLocations")
@Data
public class AdDisplayLocations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    @ManyToOne
    @JoinColumn(name = "CampaignID", nullable = false)
    private AdminAdCampaigns campaign;

    @Column(nullable = false, length = 255)
    private String locationName;

    @Column(columnDefinition = "TEXT")
    private String locationDescription;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime displayStart;

    private LocalDateTime displayEnd;
}
