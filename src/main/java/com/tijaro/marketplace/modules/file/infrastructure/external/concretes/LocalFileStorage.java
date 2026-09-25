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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class LocalFileStorage implements IFileStorage {

    private static final int MAX_FILE_NAME_LENGTH = 255;

    private static final String IMAGE_DIRECTORY = "images";
    private static final String VIDEO_DIRECTORY = "videos";
    private static final String AUDIO_DIRECTORY = "audio";
    private static final String DOCUMENT_DIRECTORY = "documents";

    private final Path uploadsPath;
    private final String baseUrl;

    public LocalFileStorage(
            @Value("${file.storage.upload-dir}")
            String uploadDirectory,

            @Value("${file.storage.base-url}")
            String baseUrl
    ) {
        if (uploadDirectory == null || uploadDirectory.isBlank()) {
            throw new IllegalArgumentException(
                    "Upload directory must not be null or empty."
            );
        }

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException(
                    "File storage base URL must not be null or empty."
            );
        }

        this.uploadsPath = Paths
                .get(uploadDirectory)
                .toAbsolutePath()
                .normalize();

        this.baseUrl = normalizeBaseUrl(baseUrl);

        initializeUploadDirectory();
    }

    @Override
    public FileUploadResult upload(
            FileUploadRequest fileUploadRequest
    ) {
        validateUploadRequest(fileUploadRequest);

        String extension = normalizeExtension(
                fileUploadRequest.extension()
        );

        validateExtension(extension);

        validateFileNameLength(
                fileUploadRequest.count(),
                extension
        );

        Path directoryPath = null;
        Path temporaryFilePath = null;

        try {
            String directoryName =
                    chooseDirectoryNameByFileCategoryName(
                            fileUploadRequest.fileCategoryName()
                    );

            directoryPath = uploadsPath
                    .resolve(directoryName)
                    .normalize();

            validateStoragePath(directoryPath);
            createAndValidateDirectory(directoryPath);

            String uniqueFileName = generateUniqueFileName(
                    directoryPath,
                    extension,
                    fileUploadRequest.count()
            );

            Path filePath = directoryPath
                    .resolve(uniqueFileName)
                    .normalize();

            validateStoragePath(filePath);

            temporaryFilePath = Files.createTempFile(
                    directoryPath,
                    ".upload-",
                    ".tmp"
            );

            fileUploadRequest
                    .multipartFile()
                    .transferTo(temporaryFilePath);

            moveFile(
                    temporaryFilePath,
                    filePath
            );

            temporaryFilePath = null;

            String storageKey = uploadsPath
                    .relativize(filePath)
                    .toString()
                    .replace('\\', '/');

            String url = generateUrl(storageKey);

            return new FileUploadResult(
                    uniqueFileName,
                    storageKey,
                    storageKey,
                    url
            );

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to store file.",
                    e
            );

        } finally {
            deleteTemporaryFile(temporaryFilePath);
        }
    }

    @Override
    public String generateUrl(
            String storageKey
    ) {
        Path filePath = resolveStorageFilePath(storageKey);

        validateExistingRegularFile(filePath);

        String relativePath = uploadsPath
                .relativize(filePath)
                .toString()
                .replace('\\', '/');

        return baseUrl + "/" + relativePath;
    }

    @Override
    public void delete(
            FileDeleteRequest fileDeleteRequest
    ) {
        if (fileDeleteRequest == null) {
            throw new IllegalArgumentException(
                    "File delete request must not be null."
            );
        }

        Path filePath = resolveStorageFilePath(
                fileDeleteRequest.storageKey()
        );

        try {
            if (Files.isSymbolicLink(filePath)) {
                throw new IllegalStateException(
                        "Symbolic links are not allowed."
                );
            }

            if (Files.isDirectory(
                    filePath,
                    LinkOption.NOFOLLOW_LINKS
            )) {
                throw new IllegalStateException(
                        "Storage key must point to a file."
                );
            }

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to delete file.",
                    e
            );
        }
    }

    private Path resolveStorageFilePath(
            String storageKey
    ) {
        if (storageKey == null || storageKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Storage key must not be null or empty."
            );
        }

        Path storageKeyPath;

        try {
            storageKeyPath = Paths.get(storageKey);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(
                    "Invalid storage key.",
                    e
            );
        }

        if (storageKeyPath.isAbsolute()) {
            throw new IllegalArgumentException(
                    "Storage key must be relative."
            );
        }

        Path filePath = uploadsPath
                .resolve(storageKeyPath)
                .normalize();

        validateStoragePath(filePath);
        validateParentDirectories(filePath);

        return filePath;
    }

    private void validateUploadRequest(
            FileUploadRequest fileUploadRequest
    ) {
        if (fileUploadRequest == null) {
            throw new IllegalArgumentException(
                    "File upload request must not be null."
            );
        }

        if (fileUploadRequest.multipartFile() == null) {
            throw new IllegalArgumentException(
                    "Multipart file must not be null."
            );
        }

        if (fileUploadRequest.fileCategoryName() == null) {
            throw new IllegalArgumentException(
                    "File category name must not be null."
            );
        }
    }

    private void initializeUploadDirectory() {
        try {
            Files.createDirectories(uploadsPath);

            if (Files.isSymbolicLink(uploadsPath)) {
                throw new IllegalStateException(
                        "Upload directory must not be a symbolic link."
                );
            }

            if (!Files.isDirectory(
                    uploadsPath,
                    LinkOption.NOFOLLOW_LINKS
            )) {
                throw new IllegalStateException(
                        "Upload path is not a directory."
                );
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to initialize upload directory.",
                    e
            );
        }
    }

    private void createAndValidateDirectory(
            Path directoryPath
    ) throws IOException {
        validateStoragePath(directoryPath);

        Files.createDirectories(directoryPath);

        if (Files.isSymbolicLink(directoryPath)) {
            throw new IllegalStateException(
                    "Storage directory must not be a symbolic link."
            );
        }

        if (!Files.isDirectory(
                directoryPath,
                LinkOption.NOFOLLOW_LINKS
        )) {
            throw new IllegalStateException(
                    "Invalid storage directory."
            );
        }

        validateParentDirectories(directoryPath);
    }

    private void validateParentDirectories(
            Path path
    ) {
        Path currentPath = path;

        if (!Files.isDirectory(
                currentPath,
                LinkOption.NOFOLLOW_LINKS
        )) {
            currentPath = currentPath.getParent();
        }

        while (currentPath != null
                && !currentPath.equals(uploadsPath)) {

            if (Files.isSymbolicLink(currentPath)) {
                throw new IllegalStateException(
                        "Symbolic links are not allowed in storage path."
                );
            }

            if (!Files.isDirectory(
                    currentPath,
                    LinkOption.NOFOLLOW_LINKS
            )) {
                throw new IllegalStateException(
                        "Invalid storage path."
                );
            }

            currentPath = currentPath.getParent();
        }

        if (!uploadsPath.equals(currentPath)) {
            throw new IllegalStateException(
                    "Invalid storage path."
            );
        }
    }

    private void validateStoragePath(
            Path path
    ) {
        Path normalizedPath = path
                .toAbsolutePath()
                .normalize();

        if (!normalizedPath.startsWith(uploadsPath)) {
            throw new IllegalStateException(
                    "Invalid storage path."
            );
        }
    }

    private void validateExistingRegularFile(
            Path filePath
    ) {
        if (Files.isSymbolicLink(filePath)) {
            throw new IllegalStateException(
                    "Symbolic links are not allowed."
            );
        }

        if (!Files.exists(
                filePath,
                LinkOption.NOFOLLOW_LINKS
        )) {
            throw new IllegalArgumentException(
                    "File not found."
            );
        }

        if (!Files.isRegularFile(
                filePath,
                LinkOption.NOFOLLOW_LINKS
        )) {
            throw new IllegalStateException(
                    "Storage key does not point to a file."
            );
        }
    }

    private String chooseDirectoryNameByFileCategoryName(
            FileCategoryName fileCategoryName
    ) {
        return switch (fileCategoryName) {
            case IMAGE -> IMAGE_DIRECTORY;
            case VIDEO -> VIDEO_DIRECTORY;
            case AUDIO -> AUDIO_DIRECTORY;
            case DOCUMENT -> DOCUMENT_DIRECTORY;
        };
    }

    private String generateUniqueFileName(
            Path directoryPath,
            String extension,
            int fileNameLength
    ) {
        String uniqueFileName;

        do {
            String uniqueFileIdentifier =
                    generateUniqueFileIdentifier(fileNameLength);

            uniqueFileName =
                    uniqueFileIdentifier + "." + extension;

        } while (Files.exists(
                directoryPath.resolve(uniqueFileName),
                LinkOption.NOFOLLOW_LINKS
        ));

        return uniqueFileName;
    }

    private void validateExtension(
            String extension
    ) {
        if (extension == null || extension.isBlank()) {
            throw new IllegalArgumentException(
                    "File extension must not be null or empty."
            );
        }

        if (!extension.matches("[a-z0-9]+")) {
            throw new IllegalArgumentException(
                    "Invalid file extension."
            );
        }
    }

    private String normalizeExtension(
            String extension
    ) {
        if (extension == null) {
            return null;
        }

        return extension
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private void validateFileNameLength(
            int fileNameLength,
            String extension
    ) {
        if (fileNameLength <= 0) {
            throw new IllegalArgumentException(
                    "File name length must be greater than zero."
            );
        }

        if (extension == null || extension.isBlank()) {
            throw new IllegalArgumentException(
                    "File extension must not be null or empty."
            );
        }

        int extensionLength = extension.length();

        if (fileNameLength + 1 + extensionLength
                > MAX_FILE_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "File name exceeds the allowed length."
            );
        }
    }

    private String generateUniqueFileIdentifier(
            int length
    ) {
        CharacterType currentStep =
                CharacterType.UPPERCASE;

        StringBuilder stringBuilder =
                new StringBuilder(length);

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

        for (int index = 0; index < length; index++) {

            if (currentStep.equals(
                    CharacterType.UPPERCASE
            )) {
                stringBuilder.append(
                        uppercaseLetters[
                                generateRandomInt(
                                        uppercaseLetters.length
                                )
                                ]
                );

                currentStep =
                        CharacterType.LOWERCASE;

            } else if (currentStep.equals(
                    CharacterType.LOWERCASE
            )) {
                stringBuilder.append(
                        lowercaseLetters[
                                generateRandomInt(
                                        lowercaseLetters.length
                                )
                                ]
                );

                currentStep =
                        CharacterType.DIGIT;

            } else {
                stringBuilder.append(
                        digits[
                                generateRandomInt(
                                        digits.length
                                )
                                ]
                );

                currentStep =
                        CharacterType.UPPERCASE;
            }
        }

        return stringBuilder.toString();
    }

    private int generateRandomInt(
            int length
    ) {
        return ThreadLocalRandom
                .current()
                .nextInt(length);
    }

    private void moveFile(
            Path temporaryFilePath,
            Path targetFilePath
    ) throws IOException {

        try {
            Files.move(
                    temporaryFilePath,
                    targetFilePath,
                    StandardCopyOption.ATOMIC_MOVE
            );

        } catch (java.nio.file.AtomicMoveNotSupportedException e) {

            Files.move(
                    temporaryFilePath,
                    targetFilePath
            );
        }
    }

    private void deleteTemporaryFile(
            Path temporaryFilePath
    ) {
        if (temporaryFilePath == null) {
            return;
        }

        try {
            Files.deleteIfExists(temporaryFilePath);
        } catch (IOException ignored) {
        }
    }

    private String normalizeBaseUrl(
            String baseUrl
    ) {
        String normalizedUrl = baseUrl.trim();

        while (normalizedUrl.endsWith("/")) {
            normalizedUrl =
                    normalizedUrl.substring(
                            0,
                            normalizedUrl.length() - 1
                    );
        }

        try {
            URI uri = new URI(normalizedUrl);

            if (uri.getScheme() == null
                    || uri.getHost() == null) {
                throw new IllegalArgumentException(
                        "File storage base URL must be an absolute URL."
                );
            }

            if (!uri.getScheme().equalsIgnoreCase("http")
                    && !uri.getScheme().equalsIgnoreCase("https")) {
                throw new IllegalArgumentException(
                        "File storage base URL must use HTTP or HTTPS."
                );
            }

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(
                    "Invalid file storage base URL.",
                    e
            );
        }

        return normalizedUrl;
    }
}