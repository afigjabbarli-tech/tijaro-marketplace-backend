package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.models.FileFormat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaFileFormatRepository extends JpaRepository<FileFormat, UUID> {

    Optional<FileFormat> findByExtensionAndMimeType(
            String extension,
            String mimeType
    );
}