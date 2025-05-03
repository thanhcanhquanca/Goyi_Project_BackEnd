package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.CommentReactionType; // Thêm import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "CommentReactions")
@Data
public class CommentReactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reactionId;

    @ManyToOne
    @JoinColumn(name = "CommentID", nullable = false)
    private PostComments comment;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentReactionType reactionType;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructor mặc định
    public CommentReactions() {
        this.createdAt = LocalDateTime.now();
    }
}