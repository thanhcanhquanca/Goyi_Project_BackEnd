package com.example.goyimanagementbackend.eNum;


public enum WalletTransactionStatus {
    PENDING,        // Giao dịch đang chờ xử lý
    PROCESSING,     // Giao dịch đang được xử lý
    COMPLETED,      // Giao dịch đã hoàn thành
    FAILED,         // Giao dịch thất bại
    CANCELLED,      // Giao dịch đã hủy
    REFUNDED,       // Giao dịch đã hoàn tiền
    EXPIRED         // Giao dịch đã hết hạn
}
