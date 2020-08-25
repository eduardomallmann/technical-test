package com.eduardomallmann.compasso.technicaltest.exceptions;

import com.eduardomallmann.compasso.technicaltest.utils.Response;
import com.eduardomallmann.compasso.technicaltest.utils.ResponseContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.Objects;

/**
 * Class responsible for filtering the errors and response them in a global standard to the origin.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Handles object search not found exception conflict response to the origin.
     *
     * @param ex exception thrown
     *
     * @return the exceptions in a error message standard inside a response entity.
     */
    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<Response<ResponseContent>> handleBusinessExceptions(final BusinessException ex) {
        return ResponseEntity.status(ex.getErrorMessage().getStatus())
                       .body(Response.of(ResponseContent.builder()
                                                 .status(HttpStatus.valueOf(ex.getErrorMessage().getStatus()).getReasonPhrase())
                                                 .errorMessage(ex.getErrorMessage())
                                                 .build()));
    }

    /**
     * Handles the global exceptions.
     *
     * @param ex exception thrown
     *
     * @return the exceptions in a error message standard inside a response entity.
     */
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Response<ResponseContent>> handleExceptions(final Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(Response.of(ResponseContent.builder()
                                                        .status(HttpStatus.valueOf(errorMessage.getStatus()).getReasonPhrase())
                                                        .errorMessage(errorMessage)
                                                        .build()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Transforms the validation exceptions into an application exception to be handle by the consumer.
     *
     * @param ex      Exception thrown
     * @param headers response headers
     * @param status  response status
     * @param request http request
     *
     * @return the exceptions in a error message standard inside a response entity.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(Objects.requireNonNull(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
        errorMessage.setErrors(Collections.singletonList(ex.getMessage()));
        errorMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        log.error("Handling constraint validation error: {} for {}", errorMessage.getMessage(), ex.getBindingResult().getTarget());
        return new ResponseEntity<>(Response.of(ResponseContent.builder()
                                                        .status(HttpStatus.valueOf(errorMessage.getStatus()).getReasonPhrase())
                                                        .errorMessage(errorMessage)
                                                        .build()), HttpStatus.BAD_REQUEST);
    }
}
