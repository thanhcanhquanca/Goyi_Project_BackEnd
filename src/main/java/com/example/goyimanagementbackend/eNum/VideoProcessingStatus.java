package com.example.goyimanagementbackend.eNum;

public enum VideoProcessingStatus {
    PENDING,        // Đang chờ xử lý
    PROCESSING,     // Đang trong quá trình xử lý
    COMPLETED,      // Đã xử lý thành công
    FAILED,         // Xử lý thất bại
    CANCELLED       // Đã hủy xử lý
}
