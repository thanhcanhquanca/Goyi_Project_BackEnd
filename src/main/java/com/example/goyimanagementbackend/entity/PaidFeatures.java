package com.example.goyimanagementbackend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "PaidFeatures")
@Data
public class PaidFeatures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer featureId;

    @Column(nullable = false, unique = true, length = 255)
    private String featureName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
