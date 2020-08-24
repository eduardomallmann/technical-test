package com.eduardomallmann.compasso.technicaltest.domains.client.validators;

import com.eduardomallmann.compasso.technicaltest.domains.client.ClientDTO;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that validates the {@link ClientDTO} object for its city and state properties. Both must be fulfilled or blank together.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ClientCityValidator.class})
public @interface ClientCityValidation {

    String message() default "{client.city.validation.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
