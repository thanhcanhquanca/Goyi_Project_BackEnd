package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.CopyrightDisputeStatus; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "CopyrightDisputes")
@Data
public class CopyrightDisputes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer disputeId;

    @ManyToOne
    @JoinColumn(name = "ClaimID", nullable = false)
    private CopyrightClaims claim;

    @ManyToOne
    @JoinColumn(name = "VideoID", nullable = false)
    private Videos video;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String disputeReason;

    @Column(columnDefinition = "TEXT")
    private String disputeEvidence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CopyrightDisputeStatus disputeStatus = CopyrightDisputeStatus.PENDING; // Giá trị mặc định là PENDING

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime disputeDate;

    private LocalDateTime resolvedAt;

    @ManyToOne
    @JoinColumn(name = "AdminID")
    private Admins admin;

    // Constructor mặc định
    public CopyrightDisputes() {
        this.disputeDate = LocalDateTime.now();
    }
}