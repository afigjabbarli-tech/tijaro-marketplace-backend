package com.tijaro.marketplace.modules.geography.presentation.validators.country;

import jakarta.validation.Payload;

public @interface ValidCountryFlag {
    String message() default "Invalid country flag!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
