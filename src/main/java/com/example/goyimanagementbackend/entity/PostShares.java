package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.SharePlatform; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "PostShares")
@Data
public class PostShares {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shareId;

    @ManyToOne
    @JoinColumn(name = "PostID", nullable = false)
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SharePlatform sharePlatform = SharePlatform.OTHER; // Giá trị mặc định là OTHER

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructor mặc định
    public PostShares() {
        this.createdAt = LocalDateTime.now();
    }
}