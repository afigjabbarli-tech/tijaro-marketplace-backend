package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.models.FileCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaFileCategoryRepository extends JpaRepository<FileCategory, UUID> {
    Optional<FileCategory> findByUid(UUID uid);
}
