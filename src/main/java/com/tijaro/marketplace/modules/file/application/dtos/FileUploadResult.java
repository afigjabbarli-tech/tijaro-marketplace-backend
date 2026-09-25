package com.tijaro.marketplace.modules.file.application.dtos;

public record FileUploadResult(
        String storedName,
        String relativePath,
        String storageKey,
        String url
) {
}