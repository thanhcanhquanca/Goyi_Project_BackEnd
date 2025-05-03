package com.example.goyimanagementbackend.eNum;


public enum PayoutRequestStatus {
    PENDING,    // Đang chờ xử lý
    APPROVED,   // Đã được duyệt
    REJECTED,   // Đã bị từ chối
    COMPLETED   // Đã hoàn thành
}
