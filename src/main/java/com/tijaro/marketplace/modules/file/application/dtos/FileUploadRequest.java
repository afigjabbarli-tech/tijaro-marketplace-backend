package com.tijaro.marketplace.modules.file.application.dtos;

import com.tijaro.marketplace.modules.file.domain.enums.FileCategoryName;
import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(MultipartFile multipartFile,
                                FileCategoryName fileCategoryName,
                                String extension,
                                int count)
{

}
