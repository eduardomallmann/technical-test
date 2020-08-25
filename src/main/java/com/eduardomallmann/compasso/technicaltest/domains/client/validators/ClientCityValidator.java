package com.eduardomallmann.compasso.technicaltest.domains.client.validators;

import com.eduardomallmann.compasso.technicaltest.domains.client.ClientDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Client data transfer validation for city and state properties, both properties must be equally fulfilled or blank together. Otherwise the application can't validate
 * the requisition.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
public class ClientCityValidator implements ConstraintValidator<ClientCityValidation, ClientDTO> {

    /**
     * Initializes the validator in preparation for {@link ClientCityValidator#isValid(ClientDTO, ConstraintValidatorContext)} calls. The constraint annotation for a
     * given constraint declaration is passed.
     * <p>
     * This method is guaranteed to be called before any use of this instance for validation.
     * <p>
     * The default implementation is a no-op.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(final ClientCityValidation constraintAnnotation) {
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
    public boolean isValid(final ClientDTO value, final ConstraintValidatorContext context) {
        final boolean hasCity = value.getCity() != null && !value.getCity().trim().isEmpty();
        final boolean hasState = value.getState() != null && !value.getState().trim().isEmpty();
        return (!hasCity && !hasState) || (hasCity && hasState);
    }
}
