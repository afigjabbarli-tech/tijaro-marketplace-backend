package com.tijaro.marketplace.modules.geography.infrastructure.persistence.services;

import com.tijaro.marketplace.common.application.exceptions.DuplicateResourceException;
import com.tijaro.marketplace.common.application.exceptions.ResourceNotFoundException;
import com.tijaro.marketplace.modules.file.domain.enums.FileOwnerType;
import com.tijaro.marketplace.modules.file.domain.enums.FilePurpose;
import com.tijaro.marketplace.modules.file.domain.enums.StorageProvider;
import com.tijaro.marketplace.modules.file.domain.models.File;
import com.tijaro.marketplace.modules.file.domain.models.FileAttachment;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileAttachmentRepository;
import com.tijaro.marketplace.modules.file.domain.repositories.IFileRepository;
import com.tijaro.marketplace.modules.geography.application.mappers.CountryMapper;
import com.tijaro.marketplace.modules.geography.application.ports.FileCreatorPort;
import com.tijaro.marketplace.modules.geography.application.ports.FileUrlProviderPort;
import com.tijaro.marketplace.modules.geography.application.services.ICountryService;
import com.tijaro.marketplace.modules.geography.domain.models.Country;
import com.tijaro.marketplace.modules.geography.domain.repositories.ICountryRepository;
import com.tijaro.marketplace.modules.geography.presentation.requests.country.CreateCountryRequest;
import com.tijaro.marketplace.modules.geography.presentation.responses.country.CountryOptionResponse;
import com.tijaro.marketplace.modules.geography.presentation.responses.country.CreateCountryResponse;
import com.tijaro.marketplace.modules.geography.presentation.responses.country.ShowCountryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService implements ICountryService {

    private final ICountryRepository countryRepository;
    private final IFileAttachmentRepository fileAttachmentRepository;
    private final IFileRepository fileRepository;
    private final CountryMapper  countryMapper;
    private final FileCreatorPort  fileCreatorPort;
    private final FileUrlProviderPort fileUrlProviderPort;

    @Override
    @Transactional
    public CreateCountryResponse createCountry(CreateCountryRequest request)
    {
        if (countryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "A country with this name already exists!"
            );
        }

        if (countryRepository.existsByOfficialName(request.getOfficial_name())) {
            throw new DuplicateResourceException(
                    "A country with this official name already exists!"
            );
        }

        if(countryRepository.existsByNativeName(request.getNative_name()))
        {
            throw new DuplicateResourceException(
                    "A country with this native name already exists!"
            );
        }

        if(countryRepository.existsByIso2Code(request.getIso2_code()))
        {
            throw new DuplicateResourceException(
                    "A country with this ISO 2 code already exists!"
            );
        }

        if(countryRepository.existsByIso3Code(request.getIso3_code()))
        {
            throw new DuplicateResourceException(
                    "A country with this ISO 3 code already exists!"
            );
        }

        if(countryRepository.existsByNumericCode(request.getNumeric_code()))
        {
            throw new DuplicateResourceException(
                    "A country with this numeric code already exists!"
            );
        }

        var country = countryMapper.mapToEntity(request);

        var savedCountry = countryRepository.save(country);

        String flagUrl = fileCreatorPort.create(request.getCountry_flag(), StorageProvider.LOCAL,
                FileOwnerType.COUNTRY, savedCountry.getUid(), FilePurpose.COUNTRY_FLAG,
                0, true);

        return countryMapper.mapToCreateResponse(savedCountry, flagUrl);
    }

    @Override
    public ShowCountryResponse getCountryByUid(UUID uid)
    {
        var country = countryRepository.findByUid(uid)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found!")
                );

        var fileAttachment = fileAttachmentRepository
                .findByOwnerUid(country.getUid())
                .orElseThrow(() -> new ResourceNotFoundException("File attachment not found!"));

        var file = fileRepository
                .findByUid(fileAttachment.getFileUid())
                .orElseThrow(() -> new ResourceNotFoundException("File not found!"));

        String flagUrl = fileUrlProviderPort
                .generate(file.getStorageKey());

        return countryMapper.mapToShowResponse(country, flagUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowCountryResponse> getAllCountries() {

        var countries = countryRepository.findAll();

        if (countries.isEmpty()) {
            return List.of();
        }

        var countryUids = countries.stream()
                .map(Country::getUid)
                .toList();

        var attachments = fileAttachmentRepository
                .findAllByOwnerTypeAndOwnerUidInAndFilePurpose(
                        FileOwnerType.COUNTRY,
                        countryUids,
                        FilePurpose.COUNTRY_FLAG
                );

        var fileUids = attachments.stream()
                .map(FileAttachment::getFileUid)
                .toList();

        var files = fileRepository.findAllByUidIn(fileUids);

        var attachmentByOwnerUid = attachments.stream()
                .collect(Collectors.toMap(
                        FileAttachment::getOwnerUid,
                        Function.identity()
                ));

        var fileByUid = files.stream()
                .collect(Collectors.toMap(
                        File::getUid,
                        Function.identity()
                ));

        return countries.stream()
                .map(country -> {

                    var attachment = attachmentByOwnerUid.get(country.getUid());

                    if (attachment == null) {
                        throw new ResourceNotFoundException(
                                "Country flag attachment not found!"
                        );
                    }

                    var file = fileByUid.get(attachment.getFileUid());

                    if (file == null) {
                        throw new ResourceNotFoundException(
                                "Country flag file not found!"
                        );
                    }

                    var flagUrl = fileUrlProviderPort
                            .generate(file.getStorageKey());

                    return countryMapper.mapToShowResponse(
                            country,
                            flagUrl
                    );
                })
                .toList();
    }
    public List<CountryOptionResponse> getAllCountryOptions()
    {
        var countries = countryRepository.findAll();
        if (countries.isEmpty())
        {
            return List.of();
        }

        var countryUids = countries.stream()
                .map(Country::getUid)
                .toList();

        var attachments = fileAttachmentRepository
                .findAllByOwnerTypeAndOwnerUidInAndFilePurpose(FileOwnerType.COUNTRY,
                        countryUids, FilePurpose.COUNTRY_FLAG);

        var fileUids = attachments.stream()
                .map(FileAttachment::getFileUid)
                .toList();

        var files = fileRepository.findAllByUidIn(fileUids);

        var attachmentByOwnerUid = attachments.stream()
                .collect(Collectors.toMap(
                        FileAttachment::getOwnerUid,
                        Function.identity()
                ));

        var fileByUid = files.stream()
                .collect(Collectors.toMap(
                        File::getUid,
                        Function.identity()
                ));

        return countries.stream()
                .map(country -> {

                    var attachment = attachmentByOwnerUid.get(country.getUid());

                    if (attachment == null) {
                        throw new ResourceNotFoundException(
                                "Country flag attachment not found!"
                        );
                    }

                    var file = fileByUid.get(attachment.getFileUid());

                    if (file == null) {
                        throw new ResourceNotFoundException(
                                "Country flag file not found!"
                        );
                    }

                    var flagUrl = fileUrlProviderPort
                            .generate(file.getStorageKey());

                    return countryMapper.mapToOptionResponse(
                            country,
                            flagUrl
                    );
                })
                .toList();
    }
}
