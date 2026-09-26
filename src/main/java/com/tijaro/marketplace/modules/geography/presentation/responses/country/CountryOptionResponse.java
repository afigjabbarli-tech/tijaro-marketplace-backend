package com.tijaro.marketplace.modules.geography.presentation.responses.country;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CountryOptionResponse {
    private UUID uid;
    private String name;
    private String phone_code;
    private String flag_url;
}
