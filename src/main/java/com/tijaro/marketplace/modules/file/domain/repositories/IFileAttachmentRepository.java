package com.tijaro.marketplace.modules.file.domain.repositories;

import com.tijaro.marketplace.modules.file.domain.models.FileAttachment;

public interface IFileAttachmentRepository {
    FileAttachment save(FileAttachment fileAttachment);
}
