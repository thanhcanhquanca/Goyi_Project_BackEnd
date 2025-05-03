package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.PaymentMethod;     // Thêm import
import com.example.goyimanagementbackend.eNum.TransactionStatus; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "Transactions")
@Data
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "PlanID", nullable = false)
    private SubscriptionPlans plan;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(length = 100)
    private String transactionReference;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING; // Giá trị mặc định là PENDING

    // Constructor mặc định
    public Transactions() {
        this.transactionDate = LocalDateTime.now();
    }

    // Helper method để kiểm tra trạng thái giao dịch
    public boolean isSuccessful() {
        return this.status == TransactionStatus.SUCCESS;
    }
}