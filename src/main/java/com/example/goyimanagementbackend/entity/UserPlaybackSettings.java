package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.VideoQuality; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "UserPlaybackSettings")
@Data
public class UserPlaybackSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer settingId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Column(precision = 3, scale = 1)
    private BigDecimal defaultPlaybackSpeed = BigDecimal.valueOf(1.0);

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoQuality defaultVideoQuality = VideoQuality.AUTO; // Giá trị mặc định là AUTO

    private Integer defaultStopAfterMinutes;

    private Boolean defaultIsLoopEnabled = false;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Constructor mặc định
    public UserPlaybackSettings() {
        this.defaultPlaybackSpeed = BigDecimal.valueOf(1.0);
        this.defaultIsLoopEnabled = false;
        this.updatedAt = LocalDateTime.now();
    }

    // Helper method để lấy giá trị chất lượng video dưới dạng string
    public String getVideoQualityValue() {
        return this.defaultVideoQuality.getValue();
    }
}