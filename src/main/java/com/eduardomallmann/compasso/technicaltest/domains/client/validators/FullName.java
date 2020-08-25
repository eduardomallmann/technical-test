package com.eduardomallmann.compasso.technicaltest.domains.client.validators;

import com.eduardomallmann.compasso.technicaltest.domains.client.dto.ClientResponse;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * Annotation that validates the {@link ClientResponse} object for its city and state properties. Both must be fulfilled or blank together.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FullNameValidator.class})
public @interface FullName {

    String message() default "{client.validation.full-name.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
