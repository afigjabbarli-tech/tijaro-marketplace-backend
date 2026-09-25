package com.tijaro.marketplace.modules.geography.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.geography.domain.models.Country;
import com.tijaro.marketplace.modules.geography.domain.repositories.ICountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CountryRepository implements ICountryRepository {

    private final JpaCountryRepository jpaCountryRepository;

    @Override
    public boolean existsByName(String name) {
        return jpaCountryRepository.existsByName(name);
    }

    @Override
    public boolean existsByOfficialName(String officialName) {
        return jpaCountryRepository.existsByOfficialName(officialName);
    }

    @Override
    public boolean existsByNativeName(String nativeName) {
        return jpaCountryRepository.existsByNativeName(nativeName);
    }

    @Override
    public boolean existsByIso2Code(String iso2Code) {
        return jpaCountryRepository.existsByIso2Code(iso2Code);
    }

    @Override
    public boolean existsByIso3Code(String iso3Code) {
        return jpaCountryRepository.existsByIso3Code(iso3Code);
    }

    @Override
    public boolean existsByNumericCode(String numericCode) {
        return jpaCountryRepository.existsByNumericCode(numericCode);
    }

    @Override
    public Country save(Country country)
    {
        return jpaCountryRepository.save(country);
    }

    @Override
    public Optional<Country> findByUid(UUID uid) {
        return jpaCountryRepository.findByUid(uid);
    }
}
