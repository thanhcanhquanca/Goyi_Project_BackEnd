package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.VideoCopyrightStatus; // Import enum VideoCopyrightStatus
import com.example.goyimanagementbackend.eNum.VideoStatus;          // Import enum VideoStatus
import com.example.goyimanagementbackend.converter.JsonListConverter;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Videos")
@Data
public class Videos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer videoId;                       // ID của video

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;                            // Người dùng đăng video

    @Column(nullable = false, length = 255)
    private String title;                          // Tiêu đề video

    @Column(columnDefinition = "TEXT")
    private String description;                    // Mô tả video

    @Column(nullable = false, length = 500)
    private String videoUrl;                       // Đường dẫn video

    @Column(length = 500)
    private String thumbnailUrl;                   // Đường dẫn ảnh thumbnail

    private Integer duration;                      // Thời lượng video (giây)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoStatus status = VideoStatus.PUBLIC;  // Trạng thái video, mặc định là PUBLIC

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;               // Thời điểm tạo

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;               // Thời điểm cập nhật

    private Boolean isDeleted = false;             // Đánh dấu đã xóa

    @Column(length = 255)
    private String copyrightOwner;                 // Chủ sở hữu bản quyền

    @Column(length = 255)
    private String copyrightLicense;               // Giấy phép bản quyền

    private LocalDate copyrightIssueDate;          // Ngày cấp bản quyền

    private LocalDate copyrightExpiryDate;         // Ngày hết hạn bản quyền

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoCopyrightStatus copyrightStatus = VideoCopyrightStatus.PENDING;  // Trạng thái bản quyền

    @Convert(converter = JsonListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> availableQualities;       // Danh sách chất lượng video có sẵn

    // Constructor mặc định
    public Videos() {
        this.createdAt = LocalDateTime.now();      // Khởi tạo thời gian tạo
        this.updatedAt = LocalDateTime.now();      // Khởi tạo thời gian cập nhật
        this.isDeleted = false;                    // Mặc định chưa xóa
        this.status = VideoStatus.PUBLIC;          // Mặc định là công khai
    }

    // Helper method để cập nhật thời gian
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();      // Cập nhật thời gian khi có thay đổi
    }

    // Helper method để kiểm tra bản quyền còn hiệu lực
    public boolean isCopyrightValid() {
        if (this.copyrightExpiryDate == null) {
            return this.copyrightStatus == VideoCopyrightStatus.VERIFIED ||
                    this.copyrightStatus == VideoCopyrightStatus.LICENSED ||
                    this.copyrightStatus == VideoCopyrightStatus.FAIR_USE;
        }
        return !this.copyrightExpiryDate.isBefore(LocalDate.now()) &&
                (this.copyrightStatus == VideoCopyrightStatus.VERIFIED ||
                        this.copyrightStatus == VideoCopyrightStatus.LICENSED);
    }

    // Helper method để kiểm tra video có thể xem được
    public boolean isViewable() {
        return !this.isDeleted &&
                this.status == VideoStatus.PUBLIC &&
                this.copyrightStatus != VideoCopyrightStatus.VIOLATED &&
                this.copyrightStatus != VideoCopyrightStatus.REMOVED;
    }

    // Helper method để kiểm tra video có đang bị chặn
    public boolean isBlocked() {
        return this.status == VideoStatus.BLOCKED;
    }

    // Helper method để kiểm tra video có đang được xem xét
    public boolean isUnderReview() {
        return this.status == VideoStatus.UNDER_REVIEW;
    }
}