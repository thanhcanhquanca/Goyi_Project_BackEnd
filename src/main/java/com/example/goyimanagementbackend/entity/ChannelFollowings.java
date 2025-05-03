package com.example.goyimanagementbackend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ChannelFollowings")
@Data
public class ChannelFollowings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer followingId;

    @ManyToOne
    @JoinColumn(name = "FollowerChannelID", nullable = false)
    private Users followerChannel;

    @ManyToOne
    @JoinColumn(name = "FollowedChannelID", nullable = false)
    private Users followedChannel;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}