package com.tijaro.marketplace.modules.geography.presentation.controllers;

import com.tijaro.marketplace.common.application.responses.ApiResponse;
import com.tijaro.marketplace.modules.geography.application.services.ICountryService;
import com.tijaro.marketplace.modules.geography.presentation.requests.country.CreateCountryRequest;
import com.tijaro.marketplace.modules.geography.presentation.responses.country.CreateCountryResponse;
import com.tijaro.marketplace.modules.geography.presentation.responses.country.ShowCountryResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {

    private final ICountryService countryService;

    @Operation(
            summary = "Create country",
            description = "Creates a country with its flag."
    )
    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<CreateCountryResponse>> create(
            @Valid @ModelAttribute CreateCountryRequest request
    ) {

        var createCountryResponse = countryService.createCountry(request);

        var apiResponse = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Resource created successfully.",
                createCountryResponse
        );

        URI location = URI.create(
                "/api/v1/countries/show/" + createCountryResponse.getUid()
        );

        return ResponseEntity
                .created(location)
                .body(apiResponse);
    }
    @GetMapping("/show/{uid}")
    @Operation(
            summary = "Get country by UID",
            description = "Returns a country by its UID."
    )
    public ResponseEntity<ApiResponse<ShowCountryResponse>> getByUid(
            @PathVariable("uid") UUID uid)
    {
        var showCountryResponse =
                countryService.getCountryByUid(uid);

        var apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Resource retrieved successfully.",
                showCountryResponse
        );

        return ResponseEntity
                .ok(apiResponse);
    }
    @Operation(
            summary = "Get all countries",
            description = "Returns all countries."
    )
    @GetMapping("/show/all")
    public ResponseEntity<ApiResponse<List<ShowCountryResponse>>> getAll() {

        var countries = countryService.getAllCountries();

        var apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Resources retrieved successfully.",
                countries
        );

        return ResponseEntity.ok(apiResponse);
    }
}