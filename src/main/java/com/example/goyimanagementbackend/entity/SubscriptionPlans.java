package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.SubscriptionPlanStatus; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "SubscriptionPlans")
@Data
public class SubscriptionPlans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    @Column(nullable = false, unique = true, length = 255)
    private String planName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionPlanStatus status = SubscriptionPlanStatus.ACTIVE; // Giá trị mặc định là ACTIVE

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructor mặc định
    public SubscriptionPlans() {
        this.createdAt = LocalDateTime.now();
    }

    // Helper method để kiểm tra xem gói có đang hoạt động không
    public boolean isActive() {
        return this.status == SubscriptionPlanStatus.ACTIVE;
    }
}