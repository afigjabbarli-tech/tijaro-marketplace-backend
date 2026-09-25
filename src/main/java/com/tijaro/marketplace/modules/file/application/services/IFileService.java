package com.tijaro.marketplace.modules.file.application.services;

import com.tijaro.marketplace.modules.file.application.dtos.GenerateFileDTO;

public interface IFileService {
    String generateFile(GenerateFileDTO createFileDTO);
    String generateUrl(String storageKey);
}
