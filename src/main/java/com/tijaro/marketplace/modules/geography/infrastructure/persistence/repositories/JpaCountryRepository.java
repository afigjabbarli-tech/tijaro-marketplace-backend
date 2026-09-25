package com.tijaro.marketplace.modules.geography.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.geography.domain.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaCountryRepository extends JpaRepository<Country, UUID> {
    boolean existsByName(String name);

    boolean existsByOfficialName(String officialName);

    boolean existsByNativeName(String nativeName);

    boolean existsByIso2Code(String iso2Code);

    boolean existsByIso3Code(String iso3Code);

    boolean existsByNumericCode(String numericCode);

    Optional<Country> findByUid(UUID uid);
}
