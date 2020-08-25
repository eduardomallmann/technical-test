package com.eduardomallmann.compasso.technicaltest.utils;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class responsible for encapsulate the body of the responseBuilder e return an standard object.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
public class Response<T> {

    private List<T> content;
    private long numberOfElements;
    private long pageSize;
    private long pageNumber;
    private long totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;
    private boolean empty;

    /**
     * Private constructor to be used internally.
     */
    private Response() {
    }

    /**
     * Instantiates the {@link ResponseBuilder}.
     *
     * @return a new instance of {@link ResponseBuilder}.
     */
    @SuppressWarnings("unchecked")
    public static ResponseBuilder builder() {
        return new ResponseBuilder(Builder.create(Response.class));
    }

    /**
     * Creates a new instance of {@link Response} with the list informed as content. This method should be used when the responseBuilder is a list with all the data from
     * the dataset.
     *
     * @param list the responseBuilder
     * @param <T>  The class of the object that will be sent in the content.
     *
     * @return A new instance of {@link Response}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Response<T> of(final List<T> list) {
        if (list == null || list.isEmpty()) return builder().empty(true).build();
        return builder()
                       .content(list)
                       .numberOfElements(list.size())
                       .pageSize(list.size())
                       .pageNumber(0)
                       .totalPages(1)
                       .totalElements(list.size())
                       .first(true)
                       .last(true)
                       .empty(false)
                       .build();
    }

    /**
     * Creates a new instance of {@link Response} with the object informed. This method should be used when the object to be sent is not a collection.
     *
     * @param object the object to be sent  in the content.
     * @param <T>    The class of the object that will be sent in the content.
     *
     * @return A new instance of {@link Response}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Response<T> of(final T object) {
        if (object == null) return builder().empty(true).build();
        return of(Collections.singletonList(object));
    }

    /**
     * Creates a new instance of {@link Response} with the page informed. This method should be used when the object to be sent in the responseBuilder is a {@link Page}.
     *
     * @param page the page with the content to be sent in the responseBuilder
     * @param <T>  The class of the object that will be sent in the content.
     *
     * @return A new instance of {@link Response}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Response<T> of(final Page<T> page) {
        if (page == null || page.getContent().isEmpty()) return builder().empty(true).build();
        return builder()
                       .content(page.getContent())
                       .numberOfElements(page.getNumberOfElements())
                       .pageSize(page.getSize())
                       .pageNumber(page.getNumber())
                       .totalPages(page.getTotalPages())
                       .totalElements(page.getTotalElements())
                       .first(page.isFirst())
                       .last(page.isLast())
                       .empty(page.isEmpty())
                       .build();
    }

    public static class ResponseBuilder<T> {

        public Builder<Response<T>> builder;

        public ResponseBuilder(final Builder<Response<T>> builder) {
            this.builder = builder;
        }

        public ResponseBuilder<T> content(final List<T> content) {
            this.builder.with(r -> r.content = content);
            return this;
        }

        public ResponseBuilder<T> content(final T content) {
            this.builder.with(r -> {
                if (r.content == null) {
                    r.content = new ArrayList<>();
                }
                r.content.add(content);
            });
            return this;
        }

        public ResponseBuilder<T> numberOfElements(final long numberOfElements) {
            this.builder.with(r -> r.numberOfElements = numberOfElements);
            return this;
        }

        public ResponseBuilder<T> pageSize(final long pageSize) {
            this.builder.with(r -> r.pageSize = pageSize);
            return this;
        }

        public ResponseBuilder<T> pageNumber(final long pageNumber) {
            this.builder.with(r -> r.pageNumber = pageNumber);
            return this;
        }

        public ResponseBuilder<T> totalPages(final long totalPages) {
            this.builder.with(r -> r.totalPages = totalPages);
            return this;
        }

        public ResponseBuilder<T> totalElements(final long totalElements) {
            this.builder.with(r -> r.totalElements = totalElements);
            return this;
        }

        public ResponseBuilder<T> first(final boolean first) {
            this.builder.with(r -> r.first = first);
            return this;
        }

        public ResponseBuilder<T> last(final boolean last) {
            this.builder.with(r -> r.last = last);
            return this;
        }

        public ResponseBuilder<T> empty(final boolean empty) {
            this.builder.with(r -> r.empty = empty);
            return this;
        }

        public Response<T> build() {
            return this.builder.build();
        }
    }

    public List<T> getContent() {
        return content;
    }

    public long getNumberOfElements() {
        return numberOfElements;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    public boolean isEmpty() {
        return empty;
    }
}
