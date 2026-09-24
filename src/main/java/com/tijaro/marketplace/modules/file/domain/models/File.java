package com.tijaro.marketplace.modules.file.domain.models;

import com.tijaro.marketplace.common.domain.models.BaseEntity;
import com.tijaro.marketplace.modules.file.domain.enums.StorageProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "files")
@Getter
@Setter
public class File extends BaseEntity {

    @Column(
            name = "original_name",
            nullable = false,
            length = 255,
            columnDefinition = "VARCHAR(255)"
    )
    private String originalName;

    @Column(
            name = "stored_name",
            nullable = false,
            unique = true,
            length = 255,
            columnDefinition = "VARCHAR(255)"
    )
    private String storedName;

    //Yeni elave edilib!
    @Column(
            name = "relative_path",
            nullable = false,
            length = 500,
            columnDefinition = "VARCHAR(500)"
    )
    private String relativePath;

    @Column(
            name = "storage_key",
            nullable = false,
            unique = true,
            length = 500,
            columnDefinition = "VARCHAR(500)"
    )
    private String storageKey;
    //Yeni elave edilib!

    @Column(
            name = "size",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long size;

    @Column(
            name = "checksum",
            length = 64,
            columnDefinition = "VARCHAR(64)"
    )
    private String checksum;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "storage_provider",
            nullable = false,
            length = 30,
            columnDefinition = "VARCHAR(30)"
    )
    private StorageProvider storageProvider;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "file_format_uid",
            nullable = false
    )
    private FileFormat format;
}