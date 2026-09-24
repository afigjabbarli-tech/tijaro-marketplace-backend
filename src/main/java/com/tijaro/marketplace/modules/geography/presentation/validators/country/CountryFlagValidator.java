package com.tijaro.marketplace.modules.geography.presentation.validators.country;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Map;

@Component
public class CountryFlagValidator implements ConstraintValidator<ValidCountryFlag, MultipartFile> {

    private static final long MAX_SIZE = 100 * 1024L;

    private static final Map<String, String> ALLOWED_FILES = Map.of(
            "svg", "image/svg+xml",
            "png", "image/png"
    );

    @Override
    public boolean isValid(
            MultipartFile file,
            ConstraintValidatorContext context) {

        if (file == null || file.isEmpty()) {
            return false;
        }

        if (file.getSize() > MAX_SIZE) {
            return false;
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            return false;
        }

        String extension = originalFilename
                .substring(originalFilename.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);

        String contentType = file.getContentType();

        if (!ALLOWED_FILES.containsKey(extension)) {
            return false;
        }

        return ALLOWED_FILES.get(extension).equals(contentType);
    }
}