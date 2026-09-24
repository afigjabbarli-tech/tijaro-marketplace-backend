package com.tijaro.marketplace.modules.file.domain.models;

import com.tijaro.marketplace.common.domain.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(
        name = "file_formats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_file_formats_extension_mime_type",
                        columnNames = {"extension", "mime_type"}
                )
        }
)
@Getter
public class FileFormat extends BaseEntity {

    @Column(
            name = "extension",
            nullable = false,
            length = 20,
            columnDefinition = "VARCHAR(20)"
    )
    private String extension;

    @Column(
            name = "mime_type",
            nullable = false,
            length = 150,
            columnDefinition = "VARCHAR(150)"
    )
    private String mimeType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "file_category_uid",
            nullable = false
    )
    private FileCategory category;
}