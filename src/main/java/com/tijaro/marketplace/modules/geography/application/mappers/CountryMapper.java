package com.tijaro.marketplace.modules.geography.application.mappers;

import com.tijaro.marketplace.modules.geography.domain.models.Country;
import com.tijaro.marketplace.modules.geography.presentation.requests.country.CreateCountryRequest;
import com.tijaro.marketplace.modules.geography.presentation.responses.country.CreateCountryResponse;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {
    public Country mapToEntity(CreateCountryRequest request)
    {
        var country = new Country();

        country.setName(request.getName());
        country.setOfficialName(request.getOfficial_name());
        country.setNativeName(request.getNative_name());
        country.setDescription(request.getDescription());
        country.setIso2Code(request.getIso2_code());
        country.setIso3Code(request.getIso3_code());
        country.setNumericCode(request.getNumeric_code());
        country.setPhoneCode(request.getPhone_code());
        country.setCapital(request.getCapital());
        country.setPopulation(request.getPopulation());
        country.setAreaKm2(request.getArea_km2());

        return country;
    }

    public CreateCountryResponse mapToResponse(Country country) {

        var response = new CreateCountryResponse();

        response.setUid(country.getUid());
        response.setName(country.getName());
        response.setOfficial_name(country.getOfficialName());
        response.setNative_name(country.getNativeName());
        response.setDescription(country.getDescription());
        response.setIso2_code(country.getIso2Code());
        response.setIso3_code(country.getIso3Code());
        response.setNumeric_code(country.getNumericCode());
        response.setPhone_code(country.getPhoneCode());
        response.setCapital(country.getCapital());
        response.setPopulation(country.getPopulation());
        response.setArea_km2(country.getAreaKm2());

        response.setCreated_at(country.getCreatedAt());
        response.setCreated_by(country.getCreatedBy());
        response.setModified_at(country.getModifiedAt());
        response.setModified_by(country.getModifiedBy());

        return response;
    }
}
