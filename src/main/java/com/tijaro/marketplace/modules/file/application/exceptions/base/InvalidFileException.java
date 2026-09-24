package com.tijaro.marketplace.modules.file.application.exceptions.base;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String message) {
        super(message);
    }
}
