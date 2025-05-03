package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.StatisticsType; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "Statistics")
@Data
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statId;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatisticsType statType = StatisticsType.DAILY; // Giá trị mặc định là DAILY

    @Column(nullable = false)
    private LocalDate statDate;

    private Integer totalVideoViews = 0;

    private Integer totalLikes = 0;

    private Integer totalSubscriptions = 0;

    private Integer totalUsersWhoWatched = 0;

    private Integer totalUsersSubscribed = 0;

    private Integer totalUsersLiked = 0;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructor mặc định
    public Statistics() {
        this.createdAt = LocalDateTime.now();
        this.statDate = LocalDate.now();
        this.totalVideoViews = 0;
        this.totalLikes = 0;
        this.totalSubscriptions = 0;
        this.totalUsersWhoWatched = 0;
        this.totalUsersSubscribed = 0;
        this.totalUsersLiked = 0;
    }
}