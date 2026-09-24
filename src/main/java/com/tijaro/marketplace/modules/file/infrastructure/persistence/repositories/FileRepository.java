package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.models.File;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileRepository implements IFileRepository {

    private final JpaFileRepository jpaFileRepository;

    @Override
    public File save(File file) {
        return jpaFileRepository.save(file);
    }
}
