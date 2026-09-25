package com.tijaro.marketplace.modules.file.infrastructure.adapters;

import com.tijaro.marketplace.modules.file.application.services.IFileService;
import com.tijaro.marketplace.modules.geography.application.ports.FileUrlProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileUrlProviderAdapter implements FileUrlProviderPort {

    private final IFileService fileService;

    @Override
    public String generate(String storageKey) {

        return fileService.generateUrl(storageKey);
    }
}
