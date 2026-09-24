package com.tijaro.marketplace.modules.file.domain.enums;

public enum FileCategoryName {

    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT;

    public static FileCategoryName fromName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "File category name must not be null or empty."
            );
        }

        try {
            return valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unsupported file category: " + name,
                    e
            );
        }
    }
}