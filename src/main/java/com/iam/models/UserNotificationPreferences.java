package com.iam.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.iam.util.SnowflakeIdGenerator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_notification_preferences")
public class UserNotificationPreferences {
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private boolean emailNotifications = true;

    @Column(nullable = false)
    private boolean smsNotifications = false;

    @Column(nullable = false)
    private boolean pushNotifications = true;

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
}
