package com.tijaro.marketplace.modules.file.domain.models;

import com.tijaro.marketplace.common.domain.models.BaseEntity;
import com.tijaro.marketplace.modules.file.domain.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(
        name = "file_attachments",
        indexes = {
                @Index(
                        name = "idx_file_attachments_owner",
                        columnList = "owner_type, owner_uid"
                ),
                @Index(
                        name = "idx_file_attachments_file",
                        columnList = "file_uid"
                )
        }
)
public class FileAttachment extends BaseEntity {

    @Column(
            name = "file_uid",
            nullable = false,
            columnDefinition = "UUID"
    )
    private UUID fileUid;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "owner_type",
            nullable = false,
            length = 50,
            columnDefinition = "VARCHAR(50)"
    )
    private FileOwnerType ownerType;

    @Column(
            name = "owner_uid",
            nullable = false,
            columnDefinition = "UUID"
    )
    private UUID ownerUid;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "file_purpose",
            nullable = false,
            length = 50,
            columnDefinition = "VARCHAR(50)"
    )
    private FilePurpose filePurpose;

    @Column(
            name = "sort_order",
            nullable = false,
            columnDefinition = "INTEGER"
    )
    private Integer sortOrder;

    @Column(
            name = "is_primary",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    private boolean isPrimary;
}