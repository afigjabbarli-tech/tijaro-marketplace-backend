package com.tijaro.marketplace.modules.file.domain.repositories;

import com.tijaro.marketplace.modules.file.domain.models.FileCategory;

import java.util.Optional;
import java.util.UUID;

public interface IFileCategoryRepository {
    Optional<FileCategory> findByUid(UUID uid);
}
