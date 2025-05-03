package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.CopyrightActionType; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "CopyrightActions")
@Data
public class CopyrightActions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer actionId;

    @ManyToOne
    @JoinColumn(name = "ClaimID")
    private CopyrightClaims claim;

    @ManyToOne
    @JoinColumn(name = "DisputeID")
    private CopyrightDisputes dispute;

    @ManyToOne
    @JoinColumn(name = "VideoID", nullable = false)
    private Videos video;

    @ManyToOne
    @JoinColumn(name = "AdminID", nullable = false)
    private Admins admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CopyrightActionType actionType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String actionReason;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime actionDate;

    // Constructor mặc định
    public CopyrightActions() {
        this.actionDate = LocalDateTime.now();
    }
}