package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.VideoProcessingStatus; // Import enum VideoProcessingStatus
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "VideoProcessingLogs")
@Data
public class VideoProcessingLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;                          // ID của log xử lý

    @ManyToOne
    @JoinColumn(name = "VideoID", nullable = false)
    private Videos video;                           // Video được xử lý

    @Column(columnDefinition = "TEXT")
    private String processingDetails;               // Chi tiết quá trình xử lý

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoProcessingStatus processingStatus = VideoProcessingStatus.PENDING;  // Trạng thái xử lý, mặc định là PENDING

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime startTime;                // Thời điểm bắt đầu xử lý

    private LocalDateTime endTime;                  // Thời điểm kết thúc xử lý

    @Column(columnDefinition = "TEXT")
    private String errorMessage;                    // Thông báo lỗi (nếu có)

    private Integer progress;                       // Tiến độ xử lý (phần trăm)

    // Constructor mặc định
    public VideoProcessingLogs() {
        this.startTime = LocalDateTime.now();       // Khởi tạo thời gian bắt đầu
        this.progress = 0;                          // Khởi tạo tiến độ là 0%
    }

    // Helper method để cập nhật tiến độ
    public void updateProgress(Integer progress) {
        this.progress = progress;
        if (progress >= 100) {
            this.processingStatus = VideoProcessingStatus.COMPLETED;
            this.endTime = LocalDateTime.now();
        }
    }

    // Helper method để đánh dấu thất bại
    public void markAsFailed(String errorMessage) {
        this.processingStatus = VideoProcessingStatus.FAILED;
        this.errorMessage = errorMessage;
        this.endTime = LocalDateTime.now();
    }

    // Helper method để kiểm tra trạng thái xử lý
    public boolean isCompleted() {
        return this.processingStatus == VideoProcessingStatus.COMPLETED;
    }
}