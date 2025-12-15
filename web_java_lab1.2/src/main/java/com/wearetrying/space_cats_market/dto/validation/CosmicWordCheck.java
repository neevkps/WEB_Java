package com.wearetrying.space_cats_market.dto.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CosmicWordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmicWordCheck {

    String message() default "Product name must contain a cosmic word such as 'star', 'galaxy' or 'comet'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

