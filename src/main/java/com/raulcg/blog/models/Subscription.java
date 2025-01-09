package com.raulcg.blog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Subscription {

    @Id
    @Column(nullable = false, unique = true)
    private String stripeSubscriptionId;

    @NotBlank
    @Column(nullable = false)
    private String subscriptionInterval;

    @NotBlank
    @Column(nullable = false)
    private String status;

    @NotBlank
    @Column(nullable = false)
    private String planId;

    @Column(nullable = false)
    private int currentPeriodStart;

    @Column(nullable = false)
    private int currentPeriodEnd;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @JsonIgnore
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
