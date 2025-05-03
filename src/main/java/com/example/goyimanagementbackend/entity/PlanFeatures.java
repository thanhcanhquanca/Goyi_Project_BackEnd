package com.example.goyimanagementbackend.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PlanFeatures")
@Data
public class PlanFeatures {
    @Id
    @ManyToOne
    @JoinColumn(name = "PlanID", nullable = false)
    private SubscriptionPlans plan;

    @Id
    @ManyToOne
    @JoinColumn(name = "FeatureID", nullable = false)
    private PaidFeatures feature;
}
