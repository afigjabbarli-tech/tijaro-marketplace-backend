package com.tijaro.marketplace.modules.file.application.services;

import com.tijaro.marketplace.modules.file.application.dtos.CreateFileDTO;

public interface IFileService {
    void createFile(CreateFileDTO createFileDTO);
}
