package com.tijaro.marketplace.modules.file.domain.repositories;

import com.tijaro.marketplace.modules.file.domain.models.File;

import java.util.Optional;
import java.util.UUID;

public interface IFileRepository {
    File save(File file);
    Optional<File> findByUid(UUID uid);
}
