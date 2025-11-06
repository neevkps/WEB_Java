package com.wearetrying.space_cats_market.service.exception;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed for the following fields: %s";

    private final List<String> validationErrors;

    public ValidationException(List<String> validationErrors) {
        super(String.format(VALIDATION_FAILED_MESSAGE, String.join(", ", validationErrors)));
        this.validationErrors = validationErrors;
    }

}
