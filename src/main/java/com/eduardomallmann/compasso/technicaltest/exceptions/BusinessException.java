package com.eduardomallmann.compasso.technicaltest.exceptions;

import com.eduardomallmann.compasso.technicaltest.utils.MessageUtils;

/**
 * Class responsible for handle the application logic behaviours failure.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
public class BusinessException extends Exception {

    private static final long serialVersionUID = 3757539297188282975L;

    private final ErrorMessage errorMessage;

    /**
     * Main constructor, creates an error message and converts it into a system error message.
     * <p>It receives a i18n key related to a business error message and the original error message</p>
     *
     * @param key   i18n key
     * @param error system error message
     */
    public BusinessException(final String key, final String error) {
        super(error);
        this.errorMessage = ErrorMessage.builder()
                                    .status(400)
                                    .message(key)
                                    .errors(error)
                                    .build();
    }

    /**
     * Optional constructor, creates an error message and converts it into a system error message.
     * <p>It receives a i18n key related to a business error message</p>
     *
     * @param key   i18n key
     */
    public BusinessException(final String key) {
        super(MessageUtils.getMessage(key));
        this.errorMessage = ErrorMessage.builder()
                                    .status(400)
                                    .message(key)
                                    .build();
    }


    public ErrorMessage getErrorMessage() {
        return this.errorMessage;
    }
}
