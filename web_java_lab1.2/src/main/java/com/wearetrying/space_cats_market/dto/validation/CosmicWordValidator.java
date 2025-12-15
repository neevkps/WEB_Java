package com.wearetrying.space_cats_market.dto.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordCheck, String> {

    private static final List<String> COSMIC_WORDS = List.of(
            "star", "galaxy", "comet", "planet", "moon", "cosmos", "orbit", "astro"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        String lower = value.toLowerCase();
        return COSMIC_WORDS.stream().anyMatch(lower::contains);
    }
}
