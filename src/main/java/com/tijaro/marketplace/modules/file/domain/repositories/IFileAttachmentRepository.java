package com.tijaro.marketplace.modules.file.domain.repositories;

import com.tijaro.marketplace.modules.file.domain.models.FileAttachment;

import java.util.Optional;
import java.util.UUID;

public interface IFileAttachmentRepository {
    FileAttachment save(FileAttachment fileAttachment);
    Optional<FileAttachment> findByOwnerUid(UUID uid);
}
