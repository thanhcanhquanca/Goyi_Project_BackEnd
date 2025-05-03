package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.VideoReportType;    // Import enum VideoReportType
import com.example.goyimanagementbackend.eNum.VideoReportStatus;  // Import enum VideoReportStatus
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "VideoReports")
@Data
public class VideoReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;                      // ID của báo cáo

    @ManyToOne
    @JoinColumn(name = "VideoID", nullable = false)
    private Videos video;                          // Video bị báo cáo

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;                            // Người dùng báo cáo

    @Column(columnDefinition = "TEXT")
    private String reason;                         // Lý do báo cáo chi tiết

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoReportType reportType;            // Loại báo cáo

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoReportStatus status = VideoReportStatus.PENDING;  // Trạng thái xử lý báo cáo

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;               // Thời điểm tạo báo cáo

    private LocalDateTime reviewedAt;              // Thời điểm xem xét báo cáo

    @ManyToOne
    @JoinColumn(name = "ReviewedByAdminID")
    private Admins reviewedBy;                     // Admin xem xét báo cáo

    @Column(columnDefinition = "TEXT")
    private String adminComment;                   // Ghi chú của admin

    // Constructor mặc định
    public VideoReports() {
        this.createdAt = LocalDateTime.now();      // Khởi tạo thời gian tạo
        this.status = VideoReportStatus.PENDING;   // Khởi tạo trạng thái là đang chờ
    }

    // Helper method để đánh dấu đã xem xét
    public void markAsReviewed(Admins admin, String comment) {
        this.status = VideoReportStatus.REVIEWED;
        this.reviewedAt = LocalDateTime.now();
        this.reviewedBy = admin;
        this.adminComment = comment;
    }

    // Helper method để đánh dấu đã giải quyết
    public void markAsResolved(Admins admin, String comment) {
        this.status = VideoReportStatus.RESOLVED;
        this.reviewedAt = LocalDateTime.now();
        this.reviewedBy = admin;
        this.adminComment = comment;
    }

    // Helper method để kiểm tra báo cáo có đang chờ xử lý
    public boolean isPending() {
        return this.status == VideoReportStatus.PENDING;
    }

    // Helper method để kiểm tra báo cáo đã được giải quyết
    public boolean isResolved() {
        return this.status == VideoReportStatus.RESOLVED;
    }
}