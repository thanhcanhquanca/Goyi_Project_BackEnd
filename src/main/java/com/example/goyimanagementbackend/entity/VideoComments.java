package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.VideoCommentStatus; // Import enum VideoCommentStatus
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "VideoComments")
@Data
public class VideoComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;                      // ID của bình luận

    @ManyToOne
    @JoinColumn(name = "VideoID", nullable = false)
    private Videos video;                           // Video được bình luận

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;                             // Người dùng bình luận

    @ManyToOne
    @JoinColumn(name = "ParentCommentID")
    private VideoComments parentComment;            // Bình luận cha (nếu là trả lời)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String commentText;                     // Nội dung bình luận

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoCommentStatus status = VideoCommentStatus.ACTIVE;  // Trạng thái bình luận, mặc định là ACTIVE

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;                // Thời điểm tạo bình luận

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;                // Thời điểm cập nhật gần nhất

    // Constructor mặc định
    public VideoComments() {
        this.createdAt = LocalDateTime.now();       // Khởi tạo thời gian tạo
        this.updatedAt = LocalDateTime.now();       // Khởi tạo thời gian cập nhật
    }

    // Helper method để cập nhật thời gian
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();       // Cập nhật thời gian khi có thay đổi
    }

    // Helper method để kiểm tra trạng thái bình luận
    public boolean isVisible() {
        return this.status == VideoCommentStatus.ACTIVE;  // Kiểm tra bình luận có hiển thị không
    }
}