package com.tijaro.marketplace.modules.file.infrastructure.persistence.services;

import com.tijaro.marketplace.modules.file.application.dtos.CreateFileDTO;
import com.tijaro.marketplace.modules.file.application.dtos.FileDeleteRequest;
import com.tijaro.marketplace.modules.file.application.dtos.FileUploadRequest;
import com.tijaro.marketplace.modules.file.application.dtos.FileUploadResult;
import com.tijaro.marketplace.modules.file.application.exceptions.EmptyFileException;
import com.tijaro.marketplace.modules.file.application.exceptions.FileCategoryNotFoundException;
import com.tijaro.marketplace.modules.file.application.exceptions.InvalidFileMetadataException;
import com.tijaro.marketplace.modules.file.application.exceptions.UnsupportedFileFormatException;
import com.tijaro.marketplace.modules.file.application.services.IFileService;
import com.tijaro.marketplace.modules.file.domain.enums.FileCategoryName;
import com.tijaro.marketplace.modules.file.domain.models.File;
import com.tijaro.marketplace.modules.file.domain.models.FileAttachment;
import com.tijaro.marketplace.modules.file.domain.models.FileCategory;
import com.tijaro.marketplace.modules.file.domain.models.FileFormat;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileAttachmentRepository;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileFormatRepository;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileRepository;
import com.tijaro.marketplace.modules.file.infrastructure.external.abstracts.IFileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class FileService implements IFileService {

    private final IFileFormatRepository fileFormatRepository;
    private final IFileAttachmentRepository fileAttachmentRepository;
    private final IFileRepository fileRepository;
    private final IFileStorage fileStorage;

    @Override
    @Transactional
    public void createFile(
            CreateFileDTO createFileDTO
    ) {

        MultipartFile multipartFile =
                createFileDTO.file();

        if (multipartFile == null ||
                multipartFile.isEmpty()) {

            throw new EmptyFileException(
                    "File must not be null or empty!"
            );
        }

        String originalFileName =
                multipartFile.getOriginalFilename();

        String mimeType =
                multipartFile.getContentType();

        if (originalFileName == null ||
                originalFileName.isBlank()) {

            throw new InvalidFileMetadataException(
                    "File original name is missing!"
            );
        }

        if (mimeType == null ||
                mimeType.isBlank()) {

            throw new InvalidFileMetadataException(
                    "File MIME type is missing!"
            );
        }

        String extension =
                getExtension(originalFileName);

        FileFormat fileFormat =
                fileFormatRepository
                        .findByExtensionAndMimeType(
                                extension,
                                mimeType
                        )
                        .orElseThrow(() ->
                                new UnsupportedFileFormatException(
                                        "Unsupported file format: "
                                                + extension
                                                + " (" + mimeType + ")"
                                )
                        );

        FileCategory category =
                fileFormat.getCategory();

        if (category == null) {

            throw new FileCategoryNotFoundException(
                    "File category not found for format: "
                            + extension
            );
        }

        FileCategoryName fileCategoryName =
                FileCategoryName.fromName(
                        category.getName()
                );

        String checksum =
                calculateChecksum(multipartFile);

        FileUploadRequest fileUploadRequest =
                new FileUploadRequest(
                        multipartFile,
                        fileCategoryName,
                        extension,
                        128
                );

        FileUploadResult storageResult =
                fileStorage.upload(
                        fileUploadRequest
                );

        registerStorageCleanup(
                storageResult.storageKey()
        );

        File file = new File();

        file.setOriginalName(
                originalFileName
        );

        file.setStoredName(
                storageResult.storedName()
        );

        file.setRelativePath(
                storageResult.relativePath()
        );

        file.setStorageKey(
                storageResult.storageKey()
        );

        file.setSize(
                multipartFile.getSize()
        );

        file.setChecksum(checksum);

        file.setStorageProvider(
                createFileDTO.storageProvider()
        );

        file.setFormat(fileFormat);

        File savedFile =
                fileRepository.save(file);

        FileAttachment fileAttachment =
                new FileAttachment();

        fileAttachment.setFileUid(
                savedFile.getUid()
        );

        fileAttachment.setOwnerType(
                createFileDTO.fileOwnerType()
        );

        fileAttachment.setOwnerUid(
                createFileDTO.fileOwnerUid()
        );

        fileAttachment.setFilePurpose(
                createFileDTO.filePurpose()
        );

        fileAttachment.setSortOrder(
                createFileDTO.sortOrder()
        );

        fileAttachment.setPrimary(
                createFileDTO.isPrimary()
        );

        fileAttachmentRepository.save(
                fileAttachment
        );
    }

    private void registerStorageCleanup(
            String storageKey
    ) {

        if (!TransactionSynchronizationManager
                .isSynchronizationActive()) {

            return;
        }

        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                            @Override
                            public void afterCompletion(
                                    int status
                            ) {

                                if (status ==
                                        TransactionSynchronization.STATUS_ROLLED_BACK) {

                                    try {

                                        fileStorage.delete(
                                                new FileDeleteRequest(
                                                        storageKey
                                                )
                                        );

                                    } catch (Exception ignored) {
                                        // Cleanup failure should be logged.
                                    }
                                }
                            }
                        }
                );
    }

    private String calculateChecksum(
            MultipartFile multipartFile
    ) {

        try {

            MessageDigest messageDigest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            try (InputStream inputStream =
                         multipartFile.getInputStream()) {

                byte[] buffer =
                        new byte[8192];

                int bytesRead;

                while ((bytesRead =
                        inputStream.read(buffer)) != -1) {

                    messageDigest.update(
                            buffer,
                            0,
                            bytesRead
                    );
                }
            }

            byte[] hashBytes =
                    messageDigest.digest();

            StringBuilder checksum =
                    new StringBuilder();

            for (byte hashByte : hashBytes) {

                checksum.append(
                        String.format(
                                "%02x",
                                hashByte
                        )
                );
            }

            return checksum.toString();

        } catch (
                NoSuchAlgorithmException |
                IOException e
        ) {

            throw new RuntimeException(
                    "Failed to calculate file checksum.",
                    e
            );
        }
    }

    private String getExtension(
            String fileName
    ) {

        int lastDotIndex =
                fileName.lastIndexOf('.');

        if (lastDotIndex == -1 ||
                lastDotIndex ==
                        fileName.length() - 1) {

            throw new InvalidFileMetadataException(
                    "File extension is missing!"
            );
        }

        return fileName
                .substring(lastDotIndex + 1)
                .toLowerCase();
    }
}