package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.models.FileFormat;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileFormatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FileFormatRepository implements IFileFormatRepository {

    private final JpaFileFormatRepository jpaFileFormatRepository;

    @Override
    public Optional<FileFormat> findByExtensionAndMimeType(
            String extension,
            String mimeType
    ) {
        return jpaFileFormatRepository.findByExtensionAndMimeType(
                extension,
                mimeType
        );
    }
}