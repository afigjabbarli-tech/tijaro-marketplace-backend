package com.tijaro.marketplace.modules.file.infrastructure.external.abstracts;

import com.tijaro.marketplace.modules.file.application.dtos.FileDeleteRequest;
import com.tijaro.marketplace.modules.file.application.dtos.FileUploadRequest;
import com.tijaro.marketplace.modules.file.application.dtos.FileUploadResult;
import org.springframework.core.io.Resource;

public interface IFileStorage{
    FileUploadResult upload(
            FileUploadRequest fileUploadRequest
    );

    void delete(
            FileDeleteRequest fileDeleteRequest
    );

    public String generateUrl(
            String storageKey
    );
}
