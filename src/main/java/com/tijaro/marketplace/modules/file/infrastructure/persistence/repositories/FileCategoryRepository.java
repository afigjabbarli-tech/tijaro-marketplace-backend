package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.models.FileCategory;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileCategoryRepository implements IFileCategoryRepository {

    private final JpaFileCategoryRepository jpaFileCategoryRepository;

    @Override
    public Optional<FileCategory> findByUid(UUID uid) {
        return jpaFileCategoryRepository.findByUid(uid);
    }
}
