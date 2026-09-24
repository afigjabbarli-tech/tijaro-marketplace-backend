package com.tijaro.marketplace.modules.file.application.exceptions;

import com.tijaro.marketplace.modules.file.application.exceptions.base.InvalidFileException;

public class InvalidFileMetadataException extends InvalidFileException {

    public InvalidFileMetadataException(String message) {
        super(message);
    }
}