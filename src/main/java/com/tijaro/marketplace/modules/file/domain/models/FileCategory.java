package com.tijaro.marketplace.modules.file.domain.models;

import com.tijaro.marketplace.common.domain.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "file_categories")
@Getter
public class FileCategory extends BaseEntity {

    @Column(
            name = "name",
            nullable = false,
            unique = true,
            length = 50,
            columnDefinition = "VARCHAR(50)"
    )
    private String name;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;
}