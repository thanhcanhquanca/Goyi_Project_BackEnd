package com.example.goyimanagementbackend.eNum;


public enum VideoCopyrightStatus {
    VERIFIED,       // Đã xác minh bản quyền
    PENDING,        // Đang chờ xác minh
    DISPUTED,       // Đang tranh chấp bản quyền
    VIOLATED,       // Vi phạm bản quyền
    LICENSED,       // Đã được cấp phép
    FAIR_USE,       // Sử dụng hợp lý
    REMOVED         // Đã gỡ do vi phạm bản quyền
}
