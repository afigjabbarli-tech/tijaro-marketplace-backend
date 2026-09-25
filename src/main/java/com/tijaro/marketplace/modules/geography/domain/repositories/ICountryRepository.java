package com.tijaro.marketplace.modules.geography.domain.repositories;

import com.tijaro.marketplace.modules.geography.domain.models.Country;

import java.util.Optional;
import java.util.UUID;

public interface ICountryRepository {

    boolean existsByName(String name);

    boolean existsByOfficialName(String officialName);

    boolean existsByNativeName(String nativeName);

    boolean existsByIso2Code(String iso2Code);

    boolean existsByIso3Code(String iso3Code);

    boolean existsByNumericCode(String numericCode);

    Country save(Country country);

    Optional<Country> findByUid(UUID uid);
}
