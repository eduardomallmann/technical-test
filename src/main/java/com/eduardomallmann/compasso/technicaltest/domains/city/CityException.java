package com.eduardomallmann.compasso.technicaltest.domains.city;

import com.eduardomallmann.compasso.technicaltest.exceptions.ErrorMessage;

/**
 * Class responsible for handle the {@link City} behaviours failure.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
public class CityException extends Exception {

    private final ErrorMessage errorMessage;

    /**
     * Main constructor, creates an error message and converts it into a system error message.
     * <p>It receives a i18n key related to a business error message and the original error message</p>
     *
     * @param key   i18n key
     * @param error system error message
     */
    public CityException(final String key, final String error) {
        super(error);
        this.errorMessage = ErrorMessage.builder()
                                    .status(404)
                                    .message(key)
                                    .errors(error)
                                    .build();
    }

    private ErrorMessage getErrorMessage() {
        return this.errorMessage;
    }
}
