package com.tijaro.marketplace.modules.file.application.exceptions;

import com.tijaro.marketplace.modules.file.application.exceptions.base.InvalidFileException;

public class FileCategoryNotFoundException extends InvalidFileException {
    public FileCategoryNotFoundException(String message) {
        super(message);
    }
}
