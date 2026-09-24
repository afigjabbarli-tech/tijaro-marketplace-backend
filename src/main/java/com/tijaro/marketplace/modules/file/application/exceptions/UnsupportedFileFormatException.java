package com.tijaro.marketplace.modules.file.application.exceptions;

import com.tijaro.marketplace.modules.file.application.exceptions.base.InvalidFileException;

public class UnsupportedFileFormatException extends InvalidFileException {
    public UnsupportedFileFormatException(String message) {
        super(message);
    }
}
