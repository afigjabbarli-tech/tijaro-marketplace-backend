package com.tijaro.marketplace.modules.geography.infrastructure.persistence.services;

import com.tijaro.marketplace.common.application.exceptions.DuplicateResourceException;
import com.tijaro.marketplace.modules.file.domain.enums.FileOwnerType;
import com.tijaro.marketplace.modules.file.domain.enums.FilePurpose;
import com.tijaro.marketplace.modules.file.domain.enums.StorageProvider;
import com.tijaro.marketplace.modules.geography.application.mappers.CountryMapper;
import com.tijaro.marketplace.modules.geography.application.ports.FileCreatorPort;
import com.tijaro.marketplace.modules.geography.application.services.ICountryService;
import com.tijaro.marketplace.modules.geography.domain.repositories.ICountryRepository;
import com.tijaro.marketplace.modules.geography.presentation.requests.country.CreateCountryRequest;
import com.tijaro.marketplace.modules.geography.presentation.responses.country.CreateCountryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CountryService implements ICountryService {

    private final ICountryRepository countryRepository;
    private final CountryMapper  countryMapper;
    private final FileCreatorPort  fileCreatorPort;

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

        fileCreatorPort.create(request.getCountry_flag(), StorageProvider.LOCAL,
                FileOwnerType.COUNTRY, savedCountry.getUid(), FilePurpose.COUNTRY_FLAG,
                0, true);

        return countryMapper.mapToResponse(savedCountry);
    }
}
