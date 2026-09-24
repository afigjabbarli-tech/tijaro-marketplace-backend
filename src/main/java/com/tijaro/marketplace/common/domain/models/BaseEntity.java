package com.tijaro.marketplace.common.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "uid",
            nullable = false,
            columnDefinition = "UUID"
    )
    private UUID uid;

    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP WITH TIME ZONE"
    )
    private Instant createdAt;

    @CreatedBy
    @Column(
            name = "created_by",
            updatable = false,
            columnDefinition = "UUID"
    )
    private UUID createdBy;

    @LastModifiedDate
    @Column(
            name = "modified_at",
            columnDefinition = "TIMESTAMP WITH TIME ZONE"
    )
    private Instant modifiedAt;

    @LastModifiedBy
    @Column(
            name = "modified_by",
            columnDefinition = "UUID"
    )
    private UUID modifiedBy;

    @Column(
            name = "deleted_at",
            columnDefinition = "TIMESTAMP WITH TIME ZONE"
    )
    private Instant deletedAt;

    @Column(
            name = "deleted_by",
            columnDefinition = "UUID"
    )
    private UUID deletedBy;

    @Column(
            name = "is_deleted",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    private boolean isDeleted;

    protected void markAsDeleted(UUID deletedBy) {

        if (isDeleted) {
            return;
        }

        this.isDeleted = true;
        this.deletedAt = Instant.now();
        this.deletedBy = deletedBy;
    }
}
