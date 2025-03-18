package com.iam.models;

import com.iam.util.SnowflakeIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "user_notification_settings")
public class UserNotificationSetting {

    @Id
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String channel; // e.g., "EMAIL", "SMS", "PUSH"

    @Column(nullable = false)
    private boolean enabled;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = SnowflakeIdGenerator.getInstance(1).nextId();
        }
    }

    @PreUpdate
    public void preUpdate() {
    }

    public UserNotificationSetting() {
    }

    public UserNotificationSetting(Long userId, String channel, boolean enabled) {
        this.userId = userId;
        this.channel = channel;
        this.enabled = enabled;
    }
}
