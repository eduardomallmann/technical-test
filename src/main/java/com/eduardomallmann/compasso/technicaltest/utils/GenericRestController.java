package com.eduardomallmann.compasso.technicaltest.utils;

import com.eduardomallmann.compasso.technicaltest.exceptions.BusinessException;
import com.eduardomallmann.compasso.technicaltest.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

/**
 * Generic interface for rest controllers, it has two methods default to be used in asynchronous calls.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
public interface GenericRestController {

    /**
     * Retrieves the search result and wraps it inside the asynchronous response.
     *
     * @param future asynchronous search result
     *
     * @return an asynchronous response with objects encapsulated in a {@link Response} object.
     */
    default <T> DeferredResult<ResponseEntity<Response<T>>> getSearchResult(final CompletableFuture<Response<T>> future) {
        DeferredResult<ResponseEntity<Response<T>>> deferredResult = new DeferredResult<>();
        future.whenCompleteAsync((result, throwable) -> {
            if (throwable != null) {
                this.addErrorMessage(deferredResult, throwable);
            } else {
                deferredResult.setResult(ResponseEntity.ok(result));
            }
        });
        return deferredResult;
    }

    /**
     * Add error message in the response of the call.
     *
     * @param deferredResult response of the call
     * @param throwable      error message
     */
    default <T> void addErrorMessage(DeferredResult<ResponseEntity<Response<T>>> deferredResult, Throwable throwable) {
        if (throwable.getCause() instanceof BusinessException) {
            ErrorMessage errorMessage = ((BusinessException) throwable.getCause()).getErrorMessage();
            deferredResult.setErrorResult(ResponseEntity.status(errorMessage.getStatus())
                                                  .body(Response.of(ResponseContent.builder()
                                                                            .status(HttpStatus.valueOf(errorMessage.getStatus()).getReasonPhrase())
                                                                            .errorMessage(errorMessage)
                                                                            .build())));
        } else {
            deferredResult.setErrorResult(throwable);
        }
    }
}
