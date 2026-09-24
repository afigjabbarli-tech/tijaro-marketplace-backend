package com.tijaro.marketplace.modules.geography.application.services;
import com.tijaro.marketplace.modules.geography.presentation.requests.country.CreateCountryRequest;
import com.tijaro.marketplace.modules.geography.presentation.responses.country.CreateCountryResponse;

public interface ICountryService {
    CreateCountryResponse createCountry(CreateCountryRequest request);
}
