package com.tijaro.marketplace.modules.file.infrastructure.external.concretes;

import com.tijaro.marketplace.common.domain.enums.CharacterType;
import com.tijaro.marketplace.modules.file.application.dtos.FileDeleteRequest;
import com.tijaro.marketplace.modules.file.application.dtos.FileUploadRequest;
import com.tijaro.marketplace.modules.file.application.dtos.FileUploadResult;
import com.tijaro.marketplace.modules.file.domain.enums.FileCategoryName;
import com.tijaro.marketplace.modules.file.infrastructure.external.abstracts.IFileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class LocalFileStorage implements IFileStorage {

    private final Path uploadsPath;

    public LocalFileStorage(
            @Value("${file.storage.upload-dir}") String uploadDirectory
    ) {
        this.uploadsPath = Paths
                .get(uploadDirectory)
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public FileUploadResult upload(
            FileUploadRequest fileUploadRequest
    ) {

        validateExtension(
                fileUploadRequest.extension()
        );

        validateCount(
                fileUploadRequest.count()
        );

        Path filePath = null;

        try {

            String directoryName =
                    chooseDirectoryNameByFileCategoryName(
                            fileUploadRequest.fileCategoryName()
                    );

            Path directoryPath = uploadsPath
                    .resolve(directoryName)
                    .normalize();

            if (!directoryPath.startsWith(uploadsPath)) {
                throw new IllegalStateException(
                        "Invalid storage directory."
                );
            }

            Files.createDirectories(directoryPath);

            String uniqueFileName =
                    generateUniqueFileName(
                            directoryPath,
                            fileUploadRequest.extension(),
                            fileUploadRequest.count()
                    );

            filePath = directoryPath
                    .resolve(uniqueFileName)
                    .normalize();

            if (!filePath.startsWith(uploadsPath)) {
                throw new IllegalStateException(
                        "Invalid storage file path."
                );
            }

            if (Files.exists(filePath)) {
                throw new IllegalStateException(
                        "File already exists: " + uniqueFileName
                );
            }

            fileUploadRequest
                    .multipartFile()
                    .transferTo(filePath);

            String relativePath = uploadsPath
                    .relativize(filePath)
                    .toString()
                    .replace('\\', '/');

            String storageKey = relativePath;

            return new FileUploadResult(
                    uniqueFileName,
                    relativePath,
                    storageKey
            );

        } catch (IOException e) {

            if (filePath != null) {
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException cleanupException) {
                    e.addSuppressed(cleanupException);
                }
            }

            throw new RuntimeException(
                    "Failed to store file.",
                    e
            );
        }
    }

    @Override
    public void delete(
            FileDeleteRequest fileDeleteRequest
    ) {

        if (fileDeleteRequest == null ||
                fileDeleteRequest.storageKey() == null ||
                fileDeleteRequest.storageKey().isBlank()) {

            throw new IllegalArgumentException(
                    "Storage key must not be null or empty."
            );
        }

        Path filePath = uploadsPath
                .resolve(fileDeleteRequest.storageKey())
                .normalize();

        if (!filePath.startsWith(uploadsPath)) {
            throw new IllegalStateException(
                    "Invalid storage file path."
            );
        }

        try {

            Files.deleteIfExists(filePath);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to delete file.",
                    e
            );
        }
    }

    private String chooseDirectoryNameByFileCategoryName(
            FileCategoryName fileCategoryName
    ) {

        if (fileCategoryName == null) {
            throw new IllegalArgumentException(
                    "File category name must not be null."
            );
        }

        return switch (fileCategoryName) {
            case IMAGE -> "images";
            case VIDEO -> "videos";
            case AUDIO -> "audio";
            case DOCUMENT -> "documents";
        };
    }

    private String generateUniqueFileName(
            Path directoryPath,
            String extension,
            int count
    ) {

        String uniqueFileName;

        do {

            String uniqueFileIdentifier =
                    generateUniqueFileIdentifier(count);

            uniqueFileName =
                    uniqueFileIdentifier + "." + extension;

        } while (Files.exists(
                directoryPath.resolve(uniqueFileName)
        ));

        return uniqueFileName;
    }

    private void validateExtension(
            String extension
    ) {

        if (extension == null ||
                extension.isBlank()) {

            throw new IllegalArgumentException(
                    "File extension must not be null or empty."
            );
        }

        if (!extension.matches("[a-zA-Z0-9]+")) {

            throw new IllegalArgumentException(
                    "Invalid file extension: " + extension
            );
        }
    }

    private void validateCount(int count) {

        if (count <= 0) {

            throw new IllegalArgumentException(
                    "File name length must be greater than zero."
            );
        }
    }

    private String generateUniqueFileIdentifier(
            int count
    ) {

        var currentStep = CharacterType.UPPERCASE;

        StringBuilder stringBuilder =
                new StringBuilder();

        char[] uppercaseLetters = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z'
        };

        char[] lowercaseLetters = {
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
                'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                'y', 'z'
        };

        char[] digits = {
                '0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9'
        };

        for (int index = 0; index < count; index++) {

            if (currentStep.equals(
                    CharacterType.UPPERCASE
            )) {

                int randomInt =
                        generateRandomInt(
                                uppercaseLetters.length
                        );

                stringBuilder.append(
                        uppercaseLetters[randomInt]
                );

                currentStep =
                        CharacterType.LOWERCASE;

            } else if (currentStep.equals(
                    CharacterType.LOWERCASE
            )) {

                int randomInt =
                        generateRandomInt(
                                lowercaseLetters.length
                        );

                stringBuilder.append(
                        lowercaseLetters[randomInt]
                );

                currentStep =
                        CharacterType.DIGIT;

            } else {

                int randomInt =
                        generateRandomInt(
                                digits.length
                        );

                stringBuilder.append(
                        digits[randomInt]
                );

                currentStep =
                        CharacterType.UPPERCASE;
            }
        }

        return stringBuilder.toString();
    }

    private int generateRandomInt(int length) {

        return java.util.concurrent.ThreadLocalRandom
                .current()
                .nextInt(length);
    }
}