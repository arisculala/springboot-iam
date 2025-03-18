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
@Table(name = "user_settings")
public class UserSettings {
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String currency = "USD";

    @Column(nullable = false)
    private String language = "en";

    @Column(nullable = false)
    private String theme = "light";

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
