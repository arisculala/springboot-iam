package com.users.management.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.users.management.util.SnowflakeIdGenerator;

@Entity
@Table(name = "user_payment_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPaymentSettings {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String preferredPaymentCurrency = "USD";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private boolean isDefault = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = SnowflakeIdGenerator.getInstance(1).nextId();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum PaymentMethod {
        CREDIT_CARD, PAYPAL, CRYPTO, BANK_TRANSFER
    }
}
