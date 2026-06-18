package com.crud.ecommerce.infrastructure.entity;

import com.crud.ecommerce.business.util.DateUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
abstract public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected final void onCreate() {
        LocalDateTime now = DateUtils.databaseNow();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected final void onUpdate() {
        updatedAt = DateUtils.databaseNow();
    }
}