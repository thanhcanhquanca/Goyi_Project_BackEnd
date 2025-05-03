package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.PayoutRequestStatus; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "PayoutRequests")
@Data
public class PayoutRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payoutId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "WalletID", nullable = false)
    private UserWallets wallet;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal netAmount;

    @Column(nullable = false, length = 100)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayoutRequestStatus status = PayoutRequestStatus.PENDING; // Giá trị mặc định là PENDING

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime requestDate;

    private LocalDateTime processedDate;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    // Constructor mặc định
    public PayoutRequests() {
        this.requestDate = LocalDateTime.now();
        this.fee = BigDecimal.ZERO;
    }

    // Helper method để tính netAmount
    @PrePersist
    @PreUpdate
    public void calculateNetAmount() {
        if (this.amount != null && this.fee != null) {
            this.netAmount = this.amount.subtract(this.fee);
        }
    }
}