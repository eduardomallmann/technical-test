package com.eduardomallmann.compasso.technicaltest.domains.client.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Client full name data transfer validation for full name property. It must contains at least two words to be validated.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
public class FullNameValidator implements ConstraintValidator<FullName, String> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Initializes the validator in preparation for {@link FullNameValidator#isValid(String, ConstraintValidatorContext)} calls. The constraint annotation for a given
     * constraint declaration is passed.
     * <p>
     * This method is guaranteed to be called before any use of this instance for validation.
     * <p>
     * The default implementation is a no-op.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(final FullName constraintAnnotation) {

    }

    /**
     * Implements the validation logic. The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        final boolean result = value != null && !value.trim().isEmpty() && value.split("\\s").length > 1;
        if (!result) log.error("Full name validation failed for: {}", value);
        return result;
    }
}
