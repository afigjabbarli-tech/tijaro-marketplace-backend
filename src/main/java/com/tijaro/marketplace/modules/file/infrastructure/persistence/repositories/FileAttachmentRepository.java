package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.enums.FileOwnerType;
import com.tijaro.marketplace.modules.file.domain.enums.FilePurpose;
import com.tijaro.marketplace.modules.file.domain.models.FileAttachment;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileAttachmentRepository implements IFileAttachmentRepository {
    private final JpaFileAttachmentRepository jpaFileAttachmentRepository;

    @Override
    public FileAttachment save(FileAttachment fileAttachment)
    {
        return jpaFileAttachmentRepository.save(fileAttachment);
    }

    public Optional<FileAttachment> findByOwnerUid(UUID uid)
    {
        return jpaFileAttachmentRepository.findByOwnerUid(uid);
    }

    @Override
    public List<FileAttachment> findAllByOwnerTypeAndOwnerUidInAndFilePurpose(FileOwnerType ownerType, List<UUID> ownerUids, FilePurpose filePurpose) {
        return jpaFileAttachmentRepository
                .findAllByOwnerTypeAndOwnerUidInAndFilePurpose(ownerType, ownerUids, filePurpose);
    }
}
