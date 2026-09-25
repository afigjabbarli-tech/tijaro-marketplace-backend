package com.tijaro.marketplace.modules.geography.presentation.responses.country;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ShowCountryResponse {

    private UUID uid;
    private String name;
    private String official_name;
    private String native_name;
    private String description;
    private String iso2_code;
    private String iso3_code;
    private String numeric_code;
    private String phone_code;
    private String capital;
    private Long population;
    private BigDecimal area_km2;

    private String flag_url;

    private Instant created_at;
    private UUID created_by;
    private Instant modified_at;
    private UUID modified_by;
}
