package com.tijaro.marketplace.modules.file.domain.repositories;

import com.tijaro.marketplace.modules.file.domain.models.FileFormat;

import java.util.Optional;

public interface IFileFormatRepository {
    Optional<FileFormat> findByExtensionAndMimeType(
            String extension,
            String mimeType);
}
