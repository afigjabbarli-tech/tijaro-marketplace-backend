package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.models.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaFileAttachmentRepository extends JpaRepository<FileAttachment, UUID> {
    Optional<FileAttachment> findByOwnerUid(UUID uid);
}
