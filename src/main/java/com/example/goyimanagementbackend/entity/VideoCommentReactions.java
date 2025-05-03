package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.ReactionType; // Import enum ReactionType
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "VideoCommentReactions")
@Data
public class VideoCommentReactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reactionId;                     // ID của phản ứng

    @ManyToOne
    @JoinColumn(name = "CommentID", nullable = false)
    private VideoComments comment;                  // Bình luận được phản ứng

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;                             // Người dùng tạo phản ứng

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType reactionType;              // Loại phản ứng

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;                // Thời điểm tạo phản ứng

    // Constructor mặc định
    public VideoCommentReactions() {
        this.createdAt = LocalDateTime.now();       // Khởi tạo thời gian tạo
    }

    // Helper method để kiểm tra loại phản ứng
    public boolean isPositiveReaction() {
        return this.reactionType == ReactionType.HEART ||
                this.reactionType == ReactionType.LIKE ||
                this.reactionType == ReactionType.WOW;
    }
}