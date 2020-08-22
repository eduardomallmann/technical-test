package com.verint.suntech.adint.customer.commons.exception;

import com.verint.suntech.adint.customer.commons.builder.Builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class responsible for the encapsulation of the error messages sent by the application.
 *
 * @author emallmann
 * @since 0.0.1
 */
public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = -362870301005498223L;

    private int status;
    private String message;
    private List<String> errors;

    public ErrorMessage() {
    }

    public ErrorMessage(final int status, final String message, final List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorMessageBuilder builder() {
        return new ErrorMessageBuilder();
    }

    public static class ErrorMessageBuilder {

        private Builder<ErrorMessage> builder;

        public ErrorMessageBuilder() {
            this.builder = Builder.create(ErrorMessage.class);
        }

        public ErrorMessageBuilder status(final int status) {
            this.builder.with(r -> r.status = status);
            return this;
        }

        public ErrorMessageBuilder message(final String message) {
            this.builder.with(r -> r.message = message);
            return this;
        }

        public ErrorMessageBuilder errors(final List<String> errors) {
            this.builder.with(r -> r.errors = errors);
            return this;
        }

        public ErrorMessageBuilder errors(final String error) {
            this.builder.with(r -> {
                if (r.errors == null) {
                    r.errors = new ArrayList<>();
                }
                r.errors.add(error);
            });
            return this;
        }

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
