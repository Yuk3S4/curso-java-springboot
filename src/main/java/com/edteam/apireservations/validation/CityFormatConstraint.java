package com.edteam.apireservations.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CityFormatValidator.class) // Indicar quién se encargará de hacer la validación
@Target( {ElementType.METHOD, ElementType.FIELD }) // scope donde esta validación va a vivir
@Retention(RetentionPolicy.RUNTIME)
public @interface CityFormatConstraint {
    String message() default "Invalid format of the city";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    // Todas las validaciones personalizadas llevan ^
}
