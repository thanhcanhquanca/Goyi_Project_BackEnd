package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.SubscriptionStatus; // Import enum SubscriptionStatus
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserSubscriptions")
@Data
public class UserSubscriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscriptionId;                  // ID của đăng ký

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;                             // Người dùng đăng ký

    @ManyToOne
    @JoinColumn(name = "PlanID", nullable = false)
    private SubscriptionPlans plan;                 // Gói đăng ký

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime startDate;                // Ngày bắt đầu đăng ký

    private LocalDateTime endDate;                  // Ngày kết thúc đăng ký

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;  // Trạng thái đăng ký, mặc định là ACTIVE

    // Constructor mặc định
    public UserSubscriptions() {
        this.startDate = LocalDateTime.now();       // Khởi tạo ngày bắt đầu
    }

    // Helper method để kiểm tra trạng thái đăng ký
    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE &&
                (this.endDate == null || this.endDate.isAfter(LocalDateTime.now()));
    }
}