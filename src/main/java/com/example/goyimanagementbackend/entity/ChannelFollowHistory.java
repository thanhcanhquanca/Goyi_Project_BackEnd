package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.ChannelFollowAction; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ChannelFollowHistory")
@Data
public class ChannelFollowHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;

    @ManyToOne
    @JoinColumn(name = "FollowerChannelID", nullable = false)
    private Users followerChannel;

    @ManyToOne
    @JoinColumn(name = "FollowedChannelID", nullable = false)
    private Users followedChannel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelFollowAction action;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime actionDate;

    // Constructor mặc định
    public ChannelFollowHistory() {
        this.actionDate = LocalDateTime.now();
    }
}