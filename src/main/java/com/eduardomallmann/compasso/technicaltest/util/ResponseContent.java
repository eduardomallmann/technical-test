package com.eduardomallmann.compasso.technicaltest.util;

/**
 * Class responsible to encapsulate the content of empty body and errors.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
public class ResponseContent {

    private String status;
    private ErrorMessage errorMessage;

    /**
     * Main constructor, empty.
     */
    public ResponseContent() {
    }

    /**
     * Optional constructor, with all arguments.
     *
     * @param status       the status of the response, if it fails or not.
     * @param errorMessage the error message object in case of error.
     */
    public ResponseContent(final String status, final ErrorMessage errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    /**
     * Method responsible to call the builder class from ResponseContentBuilder.
     *
     * @return An instance of {@link ResponseContentBuilder}
     */
    public static ResponseContentBuilder builder() {
        return new ResponseContentBuilder();
    }

    /**
     * Builder pattern implementation for {@link ResponseContent} class.
     *
     * @author emallmann
     * @since 0.0.1
     */
    public static class ResponseContentBuilder {

        private Builder<ResponseContent> builder;

        /**
         * Main constructor.
         */
        private ResponseContentBuilder() {
            this.builder = Builder.create(ResponseContent.class);
        }

        /**
         * Set the status attribute of {@link ResponseContent} object.
         *
         * @param status the status to be used
         *
         * @return The current {@link ResponseContentBuilder} object.
         */
        public ResponseContentBuilder status(final String status) {
            this.builder.with(r -> r.status = status);
            return this;
        }

        /**
         * Set the errorMessage attribute of {@link ResponseContent} object.
         *
         * @param errorMessage the errorMessage to be used
         *
         * @return The current {@link ResponseContentBuilder} object.
         */
        public ResponseContentBuilder errorMessage(final ErrorMessage errorMessage) {
            this.builder.with(r -> r.errorMessage = errorMessage);
            return this;
        }

        /**
         * Builds the {@link ResponseContent} object.
         *
         * @return The {@link ResponseContent} object instantiated.
         */
        public ResponseContent build() {
            return this.builder.build();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ResponseContent content = (ResponseContent) o;

        if (!status.equals(content.status)) return false;
        return errorMessage != null ? errorMessage.equals(content.errorMessage) : content.errorMessage == null;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseContent{" +
                       "status='" + status + '\'' +
                       ", errorMessage=" + errorMessage +
                       '}';
    }
}
