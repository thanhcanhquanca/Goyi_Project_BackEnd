package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.MonetizationStatusType; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "MonetizationStatus")
@Data
public class MonetizationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer monetizationId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MonetizationStatusType status = MonetizationStatusType.DISABLED; // Giá trị mặc định là DISABLED

    private LocalDateTime applicationDate;

    private LocalDateTime approvalDate;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalWatchHours = BigDecimal.ZERO;

    private Integer totalSubscribers = 0;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Constructor mặc định
    public MonetizationStatus() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.totalWatchHours = BigDecimal.ZERO;
        this.totalSubscribers = 0;
    }
}