package com.example.goyimanagementbackend.eNum;

public enum PaymentGatewayStatus {
    PENDING,    // Đang chờ xử lý
    COMPLETED,  // Đã hoàn thành
    FAILED,     // Thất bại
    REFUNDED    // Đã hoàn tiền
}
