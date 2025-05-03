package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.CommentReportStatus; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "CommentReports")
@Data
public class CommentReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @ManyToOne
    @JoinColumn(name = "CommentID", nullable = false)
    private PostComments comment;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reportReason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentReportStatus status = CommentReportStatus.PENDING; // Giá trị mặc định là PENDING

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructor mặc định
    public CommentReports() {
        this.createdAt = LocalDateTime.now();
    }
}