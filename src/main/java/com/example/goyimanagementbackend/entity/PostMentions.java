package com.example.goyimanagementbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "PostMentions")
@Data
public class PostMentions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mentionId;

    @ManyToOne
    @JoinColumn(name = "PostID", nullable = false)
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "MentionedUserID", nullable = false)
    private Users mentionedUser;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
