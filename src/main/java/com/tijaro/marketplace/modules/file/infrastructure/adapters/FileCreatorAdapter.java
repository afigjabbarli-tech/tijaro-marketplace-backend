package com.tijaro.marketplace.modules.file.infrastructure.adapters;

import com.tijaro.marketplace.modules.file.application.dtos.CreateFileDTO;
import com.tijaro.marketplace.modules.file.application.services.IFileService;
import com.tijaro.marketplace.modules.file.domain.enums.FileOwnerType;
import com.tijaro.marketplace.modules.file.domain.enums.FilePurpose;
import com.tijaro.marketplace.modules.file.domain.enums.StorageProvider;
import com.tijaro.marketplace.modules.geography.application.ports.FileCreatorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileCreatorAdapter implements FileCreatorPort {

    private final IFileService fileService;

    @Override
    public void create(MultipartFile file, StorageProvider  storageProvider,
                       FileOwnerType fileOwnerType, UUID fileOwnerUid,
                       FilePurpose  filePurpose, Integer sortOrder, boolean isPrimary) {

        var createFileDTO = new CreateFileDTO(file, storageProvider, fileOwnerType,
                fileOwnerUid, filePurpose, sortOrder, isPrimary);

         fileService.createFile(createFileDTO);
    }
}
