package com.tijaro.marketplace.modules.file.domain.repositories;

import com.tijaro.marketplace.modules.file.domain.models.File;

public interface IFileRepository {
    File save(File file);
}
