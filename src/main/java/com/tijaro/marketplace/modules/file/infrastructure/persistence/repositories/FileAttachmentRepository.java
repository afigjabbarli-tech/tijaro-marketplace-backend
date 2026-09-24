package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.models.FileAttachment;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileAttachmentRepository implements IFileAttachmentRepository {
    private final JpaFileAttachmentRepository jpaFileAttachmentRepository;

    @Override
    public FileAttachment save(FileAttachment fileAttachment)
    {
        return jpaFileAttachmentRepository.save(fileAttachment);
    }
}
