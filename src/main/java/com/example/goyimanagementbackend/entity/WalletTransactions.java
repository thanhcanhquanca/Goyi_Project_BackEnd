package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.WalletTransactionStatus; // Import enum WalletTransactionStatus
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "WalletTransactions")
@Data
public class WalletTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;                 // ID của giao dịch

    @ManyToOne
    @JoinColumn(name = "WalletID", nullable = false)
    private UserWallets wallet;                    // Ví liên quan đến giao dịch

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;                     // Số tiền giao dịch

    @Column(nullable = false, length = 50)
    private String transactionType;                // Loại giao dịch (nạp tiền, rút tiền, v.v.)

    @Column(length = 100)
    private String referenceId;                    // Mã tham chiếu giao dịch

    @Column(columnDefinition = "TEXT")
    private String description;                    // Mô tả giao dịch

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletTransactionStatus transactionStatus = WalletTransactionStatus.PENDING;  // Trạng thái giao dịch

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;               // Thời điểm tạo giao dịch

    private LocalDateTime completedAt;             // Thời điểm hoàn thành giao dịch

    @Column(columnDefinition = "TEXT")
    private String errorMessage;                   // Thông báo lỗi (nếu có)

    @Column(length = 50)
    private String paymentMethod;                  // Phương thức thanh toán

    // Constructor mặc định
    public WalletTransactions() {
        this.createdAt = LocalDateTime.now();      // Khởi tạo thời gian tạo
        this.transactionStatus = WalletTransactionStatus.PENDING;  // Mặc định là đang chờ
    }

    // Helper method để đánh dấu giao dịch hoàn thành
    public void markAsCompleted() {
        this.transactionStatus = WalletTransactionStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    // Helper method để đánh dấu giao dịch thất bại
    public void markAsFailed(String errorMessage) {
        this.transactionStatus = WalletTransactionStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = LocalDateTime.now();
    }

    // Helper method để đánh dấu giao dịch bị hủy
    public void markAsCancelled(String reason) {
        this.transactionStatus = WalletTransactionStatus.CANCELLED;
        this.description = reason;
        this.completedAt = LocalDateTime.now();
    }

    // Helper method để đánh dấu giao dịch hoàn tiền
    public void markAsRefunded(String reason) {
        this.transactionStatus = WalletTransactionStatus.REFUNDED;
        this.description = reason;
        this.completedAt = LocalDateTime.now();
    }

    // Helper method để kiểm tra giao dịch có thành công
    public boolean isSuccessful() {
        return this.transactionStatus == WalletTransactionStatus.COMPLETED;
    }

    // Helper method để kiểm tra giao dịch có đang xử lý
    public boolean isProcessing() {
        return this.transactionStatus == WalletTransactionStatus.PENDING ||
                this.transactionStatus == WalletTransactionStatus.PROCESSING;
    }
}