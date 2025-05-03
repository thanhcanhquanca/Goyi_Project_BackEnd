package com.example.goyimanagementbackend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "AdImpressions")
@Data
public class AdImpressions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer impressionId;

    @ManyToOne
    @JoinColumn(name = "CampaignID", nullable = false)
    private AdCampaigns campaign;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime impressionTime;
}
