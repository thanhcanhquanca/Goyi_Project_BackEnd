package com.example.goyimanagementbackend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "PostViews")
@Data
public class PostViews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer viewId;

    @ManyToOne
    @JoinColumn(name = "PostID", nullable = false)
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
