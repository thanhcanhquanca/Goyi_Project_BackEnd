package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.CopyrightClaimStatus; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "CopyrightClaims")
@Data
public class CopyrightClaims {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer claimId;

    @ManyToOne
    @JoinColumn(name = "VideoID", nullable = false)
    private Videos video;

    @ManyToOne
    @JoinColumn(name = "ClaimantUserID")
    private Users claimantUser;

    @Column(nullable = false, length = 255)
    private String claimantName;

    @Column(length = 100)
    private String claimantEmail;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String claimReason;

    @Column(columnDefinition = "TEXT")
    private String claimEvidence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CopyrightClaimStatus claimStatus = CopyrightClaimStatus.PENDING; // Giá trị mặc định là PENDING

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime claimDate;

    private LocalDateTime resolvedAt;

    @ManyToOne
    @JoinColumn(name = "AdminID")
    private Admins admin;

    // Constructor mặc định
    public CopyrightClaims() {
        this.claimDate = LocalDateTime.now();
    }
}