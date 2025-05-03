package com.example.goyimanagementbackend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "AdViews")
@Data
public class AdViews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer viewId;

    @ManyToOne
    @JoinColumn(name = "CampaignID", nullable = false)
    private AdCampaigns campaign;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime viewTime;
}
