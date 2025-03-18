package com.iam.models;

import java.time.LocalDateTime;

import com.iam.util.SnowflakeIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "clients")
public class Client {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean disabled;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = SnowflakeIdGenerator.getInstance(1).nextId();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.disabled = false;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
