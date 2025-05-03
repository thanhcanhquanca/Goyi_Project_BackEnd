package com.example.goyimanagementbackend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "VideoViews")
@Data
public class VideoViews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer viewId;

    @ManyToOne
    @JoinColumn(name = "VideoID", nullable = false)
    private Videos video;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}