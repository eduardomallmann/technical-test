package com.eduardomallmann.compasso.technicaltest.exceptions;

import com.eduardomallmann.compasso.technicaltest.utils.Builder;
import com.eduardomallmann.compasso.technicaltest.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class responsible for the encapsulation of the error messages sent by the application.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
public class ErrorMessage {

    private int status;
    private String message;
    private List<String> errors;

    /**
     * Main constructor, empty.
     */
    public ErrorMessage() {
    }

    /**
     * ErrorMessage builder instantiation.
     *
     * @return {@link ErrorMessageBuilder} object.
     */
    public static ErrorMessageBuilder builder() {
        return new ErrorMessageBuilder();
    }

    /**
     * Builder pattern implementation on {@link ErrorMessage} class.
     *
     * @author eduardomallmann
     * @since 0.0.1
     */
    public static class ErrorMessageBuilder {

        private final Builder<ErrorMessage> builder;

        /**
         * Main constructor, empty.
         */
        public ErrorMessageBuilder() {
            this.builder = Builder.create(ErrorMessage.class);
        }

        /**
         * Sets the {@link ErrorMessage#status} property.
         *
         * @param status value to be placed
         *
         * @return the {@link ErrorMessageBuilder}.
         */
        public ErrorMessageBuilder status(final int status) {
            this.builder.with(r -> r.status = status);
            return this;
        }

        /**
         * Sets the {@link ErrorMessage#message} property.
         *
         * @param message value to be placed
         *
         * @return the {@link ErrorMessageBuilder}.
         */
        public ErrorMessageBuilder message(final String message) {
            this.builder.with(r -> r.message = MessageUtils.getMessage(message));
            return this;
        }

        /**
         * Sets the {@link ErrorMessage#errors} list property.
         *
         * @param errors values to be placed
         *
         * @return the {@link ErrorMessageBuilder}.
         */
        public ErrorMessageBuilder errors(final List<String> errors) {
            this.builder.with(r -> r.errors = errors);
            return this;
        }

        /**
         * Adds a value to the {@link ErrorMessage#errors} list property.
         *
         * @param error value to be placed
         *
         * @return the {@link ErrorMessageBuilder}.
         */
        public ErrorMessageBuilder errors(final String error) {
            this.builder.with(r -> {
                if (r.errors == null) {
                    r.errors = new ArrayList<>();
                }
                r.errors.add(error);
            });
            return this;
        }

        /**
         * Instantiate the object built.
         *
         * @return an {@link ErrorMessage} object.
         */
        public ErrorMessage build() {
            return builder.build();
        }

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(final List<String> errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ErrorMessage that = (ErrorMessage) o;
        return status == that.status &&
                       message.equals(that.message) &&
                       Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, errors);
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                       "status=" + status +
                       ", message='" + message + '\'' +
                       ", errors=" + errors +
                       '}';
    }
}
