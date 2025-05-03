package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.VideoAccessType; // Import enum VideoAccessType
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "VideoAccessLogs")
@Data
public class VideoAccessLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;                          // ID của log

    @ManyToOne
    @JoinColumn(name = "VideoID", nullable = false)
    private Videos video;                           // Video được truy cập

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;                             // Người dùng thực hiện truy cập

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoAccessType accessType;             // Loại hành động truy cập

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime accessTime;               // Thời điểm truy cập

    // Constructor mặc định
    public VideoAccessLogs() {
        this.accessTime = LocalDateTime.now();      // Khởi tạo thời gian truy cập
    }
}