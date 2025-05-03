package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.PaymentGatewayType;   // Thêm import
import com.example.goyimanagementbackend.eNum.PaymentGatewayStatus; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "PaymentGatewayTransactions")
@Data
public class PaymentGatewayTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gatewayTransactionId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Column(nullable = false, length = 100)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentGatewayType gatewayType;

    @Column(length = 100)
    private String gatewayTransactionReference;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(precision = 10, scale = 2)
    private BigDecimal gatewayFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentGatewayStatus status = PaymentGatewayStatus.PENDING; // Giá trị mặc định là PENDING

    @Column(length = 50)
    private String responseCode;

    @Column(columnDefinition = "TEXT")
    private String responseMessage;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime transactionDate;

    // Constructor mặc định
    public PaymentGatewayTransactions() {
        this.transactionDate = LocalDateTime.now();
        this.gatewayFee = BigDecimal.ZERO;
    }
}