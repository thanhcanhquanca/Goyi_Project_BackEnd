package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.NotificationType; // Thêm import
import com.example.goyimanagementbackend.eNum.ReferenceType;    // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notifications")
@Data
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "SenderID")
    private Users sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false)
    private Integer referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReferenceType referenceType;

    @Column(nullable = false, length = 255)
    private String message;

    private Boolean isRead = false;

    private LocalDateTime readAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructor mặc định
    public Notifications() {
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }
}