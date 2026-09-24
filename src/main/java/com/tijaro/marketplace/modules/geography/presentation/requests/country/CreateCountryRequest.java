package com.tijaro.marketplace.modules.geography.presentation.requests.country;

import com.tijaro.marketplace.modules.geography.presentation.validators.country.ValidCountryFlag;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateCountryRequest {
    @NotBlank(message = "Country name is required and cannot be blank!")
    @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters!")
    private String name;

    @NotBlank(message = "Official name is required and cannot be blank!")
    @Size(min = 2, max = 200, message = "Official name must be between 2 and 200 characters!")
    private String official_name;

    @NotBlank(message = "Native name is required and cannot be blank!")
    @Size(min = 2, max = 100, message = "Native name must be between 2 and 100 characters!")
    private String native_name;

    @Size(min = 50, max = 2500, message = "Description must be between 50 and 2500 characters!")
    private String description;

    @NotBlank(message = "ISO 2 code is required and cannot be blank!")
    @Size(min = 2, max = 2, message = "ISO 2 code must be exactly 2 characters!")
    @Pattern(
            regexp = "^[A-Z]{2}$",
            message = "ISO 2 code must contain exactly 2 uppercase letters!"
    )
    private String iso2_code;

    @NotBlank(message = "ISO 3 code is required and cannot be blank!")
    @Size(min = 3, max = 3, message = "ISO 3 code must be exactly 3 characters!")
    @Pattern(
            regexp = "^[A-Z]{3}$",
            message = "ISO 3 code must contain exactly 3 uppercase letters!"
    )
    private String iso3_code;

    @NotBlank(message = "Numeric code is required and cannot be blank!")
    @Size(
            min = 3,
            max = 3,
            message = "Numeric code must be exactly 3 digits!"
    )
    @Pattern(
            regexp = "^[0-9]{3}$",
            message = "Numeric code must contain exactly 3 digits!"
    )
    private String numeric_code;

    @NotBlank(message = "Phone code is required and cannot be blank!")
    @Size(
            min = 2,
            max = 10,
            message = "Phone code must be between 2 and 10 characters!"
    )
    @Pattern(
            regexp = "^\\+[1-9][0-9]{0,8}$",
            message = "Phone code must start with '+' followed by 1 to 9 digits!"
    )
    private String phone_code;

    @Size(
            min = 2,
            max = 100,
            message = "Capital must be between 2 and 100 characters!"
    )
    private String capital;

    @PositiveOrZero(message = "Population must be zero or greater!")
    private Long population;

    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Area must be zero or greater!"
    )
    @Digits(
            integer = 13,
            fraction = 2,
            message = "Area must have up to 13 integer digits and 2 decimal places!"
    )
    private BigDecimal area_km2;

    @NotNull(message = "Country flag is required!")
    @ValidCountryFlag
    private MultipartFile country_flag;
}
