package com.tijaro.marketplace.modules.file.application.exceptions;

import com.tijaro.marketplace.modules.file.application.exceptions.base.InvalidFileException;

public class EmptyFileException extends InvalidFileException {
    public EmptyFileException(String message) {
        super(message);
    }
}
