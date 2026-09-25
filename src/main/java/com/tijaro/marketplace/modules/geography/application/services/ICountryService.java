package com.tijaro.marketplace.modules.geography.application.services;
import com.tijaro.marketplace.modules.geography.presentation.requests.country.CreateCountryRequest;
import com.tijaro.marketplace.modules.geography.presentation.responses.country.CreateCountryResponse;
import com.tijaro.marketplace.modules.geography.presentation.responses.country.ShowCountryResponse;

import java.util.List;
import java.util.UUID;

public interface ICountryService {
    CreateCountryResponse createCountry(CreateCountryRequest request);
    ShowCountryResponse getCountryByUid(UUID uid);
    List<ShowCountryResponse> getAllCountries();
}
