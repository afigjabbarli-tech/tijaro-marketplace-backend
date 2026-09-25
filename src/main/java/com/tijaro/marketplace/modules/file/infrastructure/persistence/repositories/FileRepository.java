package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.models.File;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileRepository implements IFileRepository {

    private final JpaFileRepository jpaFileRepository;

    @Override
    public File save(File file) {
        return jpaFileRepository.save(file);
    }

    @Override
    public Optional<File> findByUid(UUID uid)
    {
        return jpaFileRepository.findByUid(uid);
    }
}
