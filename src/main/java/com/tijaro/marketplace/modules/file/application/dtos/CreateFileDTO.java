package com.tijaro.marketplace.modules.file.application.dtos;

import com.tijaro.marketplace.modules.file.domain.enums.FileOwnerType;
import com.tijaro.marketplace.modules.file.domain.enums.FilePurpose;
import com.tijaro.marketplace.modules.file.domain.enums.StorageProvider;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreateFileDTO(MultipartFile file, StorageProvider storageProvider,
                            FileOwnerType fileOwnerType, UUID fileOwnerUid,
                            FilePurpose filePurpose, Integer sortOrder, boolean isPrimary) {
}
